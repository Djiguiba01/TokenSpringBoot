package io.getarrays.userservice.api;

import io.getarrays.userservice.domain.Role;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.repo.UserRepo;
import io.getarrays.userservice.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserRessource {
    @Autowired
    private final UserService userService;

    @Autowired
    UserRepo userRepo;

    //------------------------------------CRUD COLLABORATEUR----------------------------------->>
    @GetMapping("/collaborateurs")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/collaborateur/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User>saveCollab(@RequestBody User user, Authentication auth){
        //System.out.println(user.getUsername()+"  "+auth.getName());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/collaborateur/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }


    @PutMapping("/collaborateur/update/{ColId}")
    @PreAuthorize("hasRole('USER_ADMIN')")
    public ResponseEntity<User> updateCollab(@PathVariable("ColId") Long ColId,@RequestBody User user) {
        return ResponseEntity.ok().body(userService.updateUser(ColId,user));
    }

    @DeleteMapping("/collaborateur/delete/{userId}")
    @PreAuthorize("hasRole('USER_ADMIN')")
    public ResponseEntity<Object> deleteCollab(@PathVariable("userId") Long UserId) {
        userService.deleteUser(UserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




    //-----------------------------------ROLE----------------------------------------->
    @PostMapping("/role/save")
    @PreAuthorize("hasRole('USER_ADMIN')")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form){
         userService.addRoleToUser(form.getUsername(), form.getRoleName());
         return ResponseEntity.ok().build();
    }


}

@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}

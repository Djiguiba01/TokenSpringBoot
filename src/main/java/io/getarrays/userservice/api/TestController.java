package io.getarrays.userservice.api;

import io.getarrays.userservice.domain.AuthenticateService;
import io.getarrays.userservice.repo.RoleRepo;
import io.getarrays.userservice.repo.UserRepo;
import io.getarrays.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;

    @GetMapping("/all")
    public String allAccess(Principal all) {

        String user = "NOM D'UTILISATEUR: " + userRepo.findByUsername(all.getName()).getUsername() + "  EMAIL:  "+
                userRepo.findByUsername(all.getName()).getEmail();
        return "Bienvenue, " + user;
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String userAccess(Principal user)
    {
        return "Bienvenue, " + userRepo.findByUsername(user.getName()).getUsername() + " " +
                roleRepo.findByName(String.valueOf(AuthenticateService.ROLE_USER)).getName();
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess(Principal admin ) {

        return "Bienvenue " + " "+ userRepo.findByUsername(admin.getName()).getUsername()  + " "+
                roleRepo.findByName(String.valueOf(AuthenticateService.ROLE_ADMIN)).getName()
                ;
    }

}

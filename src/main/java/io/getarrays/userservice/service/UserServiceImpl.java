package io.getarrays.userservice.service;

import io.getarrays.userservice.domain.Role;
import io.getarrays.userservice.domain.User;
import io.getarrays.userservice.repo.RoleRepo;
import io.getarrays.userservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final RoleRepo roleRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    //Fonction qui vérifie l'existance du user
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null){
            log.error("le user {} n'existe pas la base de donnée", username);
            throw new UsernameNotFoundException(username + "n'existe pas dans la base de donnée:");
        } else {
            log.info("{} existe dans la base de donnée", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        //Affichage dans la console
        log.info("User {} ajouter avec success dans la base", user.getUsername());
        //Cryptage du mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userSave = userRepo.save(user);
        return userSave;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Un nouveau role enregistrer dans la base: {}", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Le role {} est attribué à l'utilisateur {}",roleName,username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public User findByUsername(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Affichage de tout les users");
        return userRepo.findAll();
    }


    @Override
    public Optional<User> findUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public User updateUser(Long id, User user) {

        Optional<User> retrievedUser=userRepo.findById(id);
        if(retrievedUser==null)
            try {
                throw new Exception("Utilisateur introuvable");
            } catch (Exception e) {
                e.printStackTrace();
            }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        log.info("Mise à jour effectuer avec succes");
        return userRepo.findById(id).get();

    }

    @Override
    public User deleteUser(Long userId) {

        Optional<User> retrievedUser=userRepo.findById(userId);
        if(retrievedUser==null)
            try {
                throw new Exception("Utilisateur introuvable");
            } catch (Exception e) {
                e.printStackTrace();
            }
        userRepo.deleteById(userId);
        log.info("User supprimer avec succes");
        return retrievedUser.get();
    }

}

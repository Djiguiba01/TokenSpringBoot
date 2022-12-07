package io.getarrays.userservice.service;

import io.getarrays.userservice.domain.Role;
import io.getarrays.userservice.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {


    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);

    public User findByUsername(String username);

    public List<User> getUsers();

    public Optional<User> findUserById(Long id);

    User updateUser(Long id, User user);

    User deleteUser(Long userId);
}

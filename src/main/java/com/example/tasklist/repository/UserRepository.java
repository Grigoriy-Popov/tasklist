package com.example.tasklist.repository;

import com.example.tasklist.model.user.Role;
import com.example.tasklist.model.user.User;

import java.util.Optional;

public interface UserRepository {

    User create(User user);

    Optional<User> findById(Long userId);

    Optional<User> findByUsername(String username);

    User update(User user);

    boolean delete(Long userId);

    void insertUserRole(Long userId, Role role);

    boolean checkExistenceById(Long userId);

}

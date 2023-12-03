package com.example.tasklist.service;

import com.example.tasklist.model.user.User;

public interface UserService {

    User create(User user);

    User getById(Long userId);

    User getByUsername(String username);

    User update(User user);

    void delete(Long userId);

    boolean isTaskOwner(Long userId, Long taskId);

    boolean checkExistenceById(Long userId);

}

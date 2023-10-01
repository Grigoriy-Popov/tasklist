package com.example.tasklist.service.impl;

import com.example.tasklist.model.user.User;
import com.example.tasklist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(Long userId) {
        return false;
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return false;
    }
}

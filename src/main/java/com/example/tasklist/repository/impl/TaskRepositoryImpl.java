package com.example.tasklist.repository.impl;

import com.example.tasklist.model.task.Task;
import com.example.tasklist.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository {
    @Override
    public Task create(Task task) {
        return null;
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return null;
    }

    @Override
    public Task update(Task task) {
        return null;
    }

    @Override
    public boolean delete(Long taskId) {
        return false;
    }

    @Override
    public void assignToUserById(Long taskId, Long userId) {

    }
}

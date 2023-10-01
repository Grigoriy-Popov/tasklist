package com.example.tasklist.service.impl;

import com.example.tasklist.model.task.Task;
import com.example.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Override
    public Task create(Task task) {
        return null;
    }

    @Override
    public Task getById(Long taskId) {
        return null;
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
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
}

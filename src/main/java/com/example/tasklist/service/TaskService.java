package com.example.tasklist.service;

import com.example.tasklist.model.task.Task;

import java.util.List;

public interface TaskService {

    Task create(Task task);

    Task getById(Long taskId);

    List<Task> getAllByUserId(Long userId);

    Task update(Task task);

    boolean delete(Long taskId);

}

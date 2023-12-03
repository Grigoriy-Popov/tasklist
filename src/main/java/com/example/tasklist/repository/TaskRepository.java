package com.example.tasklist.repository;

import com.example.tasklist.model.task.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task create(Task task);

    Optional<Task> findById(Long taskId);

    List<Task> findAllByUserId(Long userId);

    Task update(Task task);

    void delete(Long taskId);

    boolean checkExistence(Long taskId);

}

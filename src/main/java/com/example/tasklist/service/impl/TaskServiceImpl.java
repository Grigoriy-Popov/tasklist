package com.example.tasklist.service.impl;

import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.task.Status;
import com.example.tasklist.model.task.Task;
import com.example.tasklist.repository.TaskRepository;
import com.example.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task create(Task task) {
        task.setStatus(Status.TODO);
        taskRepository.create(task);
        return task;
    }

    @Override
    public Task getById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task was not found"));
    }

    @Override
    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    public Task update(Task task) {
        checkExistenceById(task.getId());
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.update(task);
        return task;
    }

    @Override
    @Transactional
    public void delete(Long taskId) {
        taskRepository.delete(taskId);
    }

    @Override
    public void checkExistenceById(Long taskId) {
        if (!taskRepository.checkExistence(taskId)) {
            throw new NotFoundException("Task was not found");
        }
    }

}

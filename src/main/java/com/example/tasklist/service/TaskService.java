package com.example.tasklist.service;

import com.example.tasklist.exception.NotFoundException;
import com.example.tasklist.model.task.Status;
import com.example.tasklist.model.task.Task;
import com.example.tasklist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task create(Task task) {
        task.setStatus(Status.TODO);
        taskRepository.create(task);
        return task;
    }

    @Cacheable(value = "TaskService::getByUsername", key = "#username")
    public Task getById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task was not found"));
    }

    public List<Task> getAllByUserId(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        checkExistenceById(task.getId());
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.update(task);
        return task;
    }

    @CacheEvict(value = "TaskService::getById", key = "#taskId")
    public void delete(Long taskId) {
        taskRepository.delete(taskId);
    }

    public void checkExistenceById(Long taskId) {
        if (!taskRepository.checkExistence(taskId)) {
            throw new NotFoundException("Task was not found");
        }
    }

}

package com.example.tasklist.web.controller;

import com.example.tasklist.model.task.Task;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.web.dto.task.TaskDto;
import com.example.tasklist.web.dto.validation.OnCreate;
import com.example.tasklist.web.dto.validation.OnUpdate;
import com.example.tasklist.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    @PreAuthorize("@customSecurityExpression.canAccessUser(#taskDto.userId)")
    public TaskDto create(@Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskService.create(task));
    }

    @GetMapping("/{taskId}")
    @PreAuthorize("canAccessTask(#taskId)")
    public TaskDto getById(@PathVariable long taskId) {
        return taskMapper.toDto(taskService.getById(taskId));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#userId)")
    public Collection<TaskDto> getAllByUserId(@PathVariable long userId) {
        return taskMapper.toDto(taskService.getAllByUserId(userId));
    }

    @PutMapping
    @PreAuthorize("canAccessTask(#taskDto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskService.update(task));
    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("canAccessTask(#taskId)")
    public void delete(@PathVariable long taskId) {
        taskService.delete(taskId);
    }

}

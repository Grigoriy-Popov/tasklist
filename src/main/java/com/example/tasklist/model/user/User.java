package com.example.tasklist.model.user;

import com.example.tasklist.model.task.Task;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class User {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;
    private Set<Role> roles;
    private List<Task> tasks;

    public Map<String, Object> toMap() {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("username", username);
        user.put("password_hash", password);
        user.put("roles", roles);
        user.put("tasks", tasks);
        return user;
    }

}

package com.example.tasklist.model.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime expirationDate;
    private Long userId;

    public Map<String, Object> toMap() {
        Map<String, Object> task = new HashMap<>();
        task.put("title", title);
        task.put("description", description);
        task.put("status", status);
        task.put("expiration_date", expirationDate);
        task.put("user_id", userId);
        return task;
    }

}

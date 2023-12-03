package com.example.tasklist.repository.impl;

import com.example.tasklist.model.task.Status;
import com.example.tasklist.model.task.Task;
import com.example.tasklist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository, RowMapper<Task> {

    private final JdbcTemplate jdbcTemplate;
    private final String FIND_BY_ID = """
            SELECT * FROM tasks
            WHERE id = ?""";

    private final String FIND_ALL_BY_USER_ID = """
            SELECT * FROM tasks
            WHERE user_id = ?""";

    private final String DELETE = """
            DELETE FROM tasks
            WHERE id = ?""";

    private final String UPDATE = """
            UPDATE tasks
            SET title = ?
            description = ?
            expiration_date = ?
            status = ?
            user_id = ?
            WHERE id = ?""";

    private final String CHECK_EXISTENCE = """
            SELECT COUNT(1) FROM tasks
            WHERE id = ?""";

//    private final String CREATE = """
//            INSERT INTO tasks (title, description, expiration_date, status, user_id)
//            VALUES (?, ?, ?, ?, ?)""";

    @Override
    public Task create(Task task) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasks")
                .usingGeneratedKeyColumns("id");
        task.setId(simpleJdbcInsert.executeAndReturnKey(task.toMap()).longValue());
//        return jdbcTemplate.update(CREATE, this);
        return task;
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        Task task;
        try {
            task = jdbcTemplate.queryForObject(FIND_BY_ID, this, taskId);
        } catch (EmptyResultDataAccessException e) {
            task = null;
        }
        return Optional.ofNullable(task);
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_ID, this, userId);
    }

    @Override
    public Task update(Task task) {
        jdbcTemplate.update(UPDATE, task.getTitle(), task.getDescription(), task.getExpirationDate(),
                task.getStatus(), task.getUserId(), task.getId());
        return task;
    }

    @Override
    public void delete(Long taskId) {
        jdbcTemplate.update(DELETE, taskId);
    }

    @Override
    public boolean checkExistence(Long taskId) {
        int count = jdbcTemplate.queryForObject(CHECK_EXISTENCE, Integer.class, taskId);
        return count != 0;
    }

    @Override
    @SneakyThrows
    public Task mapRow(ResultSet rs, int rowNum) {
        Timestamp timestamp = rs.getTimestamp("expiration_date");
        return Task.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .status(Status.valueOf(rs.getString("status")))
                .expirationDate(timestamp != null ? timestamp.toLocalDateTime() : null)
                .userId(rs.getLong("user_id"))
                .build();
    }

}

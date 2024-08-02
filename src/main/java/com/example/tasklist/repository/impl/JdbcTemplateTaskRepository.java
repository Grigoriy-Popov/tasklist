package com.example.tasklist.repository.impl;

import com.example.tasklist.model.task.Status;
import com.example.tasklist.model.task.Task;
import com.example.tasklist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcTemplateTaskRepository implements TaskRepository, RowMapper<Task> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Task create(Task task) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("tasks")
                .usingGeneratedKeyColumns("id");
        task.setId(simpleJdbcInsert.executeAndReturnKey(task.toMap()).longValue());
        return task;
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        String sql = "select * from tasks where id = ?";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Task.class), taskId);
        } catch (EmptyResultDataAccessException e) {
            task = null;
        }
        return Optional.ofNullable(task);
    }

    @Override
    public List<Task> findAllByUserId(Long userId) {
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
//        return jdbcTemplate.query(FIND_ALL_BY_USER_ID, this, userId);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Task.class), userId);
    }

    @Override
    public Task update(Task task) {
        String sql = "UPDATE tasks " +
                "SET title = ?, " +
                "description = ?," +
                "expiration_date = ?, " +
                "status = ?," +
                "user_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getExpirationDate(),
                task.getStatus().toString(), task.getUserId(), task.getId());
        return task;
    }

    @Override
    public void delete(Long taskId) {
        String sql = "delete from tasks where id = ?";
        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public boolean checkExistence(Long taskId) {
        String sql = "select exists(select 1 from tasks where id = ?)";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, taskId);
        return count != 0;
    }

    @Override
    @SneakyThrows
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
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

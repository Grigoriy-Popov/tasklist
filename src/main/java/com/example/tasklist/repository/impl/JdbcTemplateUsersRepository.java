package com.example.tasklist.repository.impl;

import com.example.tasklist.model.user.Role;
import com.example.tasklist.model.user.User;
import com.example.tasklist.repository.TaskRepository;
import com.example.tasklist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcTemplateUsersRepository implements UserRepository, RowMapper<User> {

    private final JdbcTemplate jdbcTemplate;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        user.getRoles().forEach(role -> insertUserRole(user.getId(), role));
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        User user;
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            user = jdbcTemplate.queryForObject(sql, this, userId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user;
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            user = jdbcTemplate.queryForObject(sql, this, username);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        String sql = "update users " +
                "set name = ?, " +
                "username = ?, " +
                "password_hash = ?";
        jdbcTemplate.update(sql, user.getName(), user.getUsername(), user.getPassword());
        return user;
    }

    @Override
    public boolean delete(Long userId) {
        String sql = "delete from users where id = ?";
        return jdbcTemplate.update(sql, userId) != 1;
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        String sql = "insert into users_roles (user_id, role) values (?, ?)";
        jdbcTemplate.update(sql, userId, role.name());
    }

    @Override
    public boolean checkExistenceById(Long userId) {
        String sql = "SELECT COUNT(1) FROM users WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != 0;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .username(rs.getString("username"))
                .password(rs.getString("password_hash"))
                .build();
        user.setRoles(getUserRoles(user.getId()));
        user.setTasks(taskRepository.findAllByUserId(user.getId()));
        return user;
    }

    private Set<Role> getUserRoles(Long userId) {
        String sql = "SELECT role FROM users_roles WHERE user_id = ?";
        return jdbcTemplate.queryForList(sql, String.class, userId).stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}

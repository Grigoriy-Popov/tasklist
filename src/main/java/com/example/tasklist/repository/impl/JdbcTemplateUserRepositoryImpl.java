package com.example.tasklist.repository.impl;

import com.example.tasklist.model.user.Role;
import com.example.tasklist.model.user.User;
import com.example.tasklist.repository.UserRepository;
import com.example.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("userJdbcTemplateRepo")
@RequiredArgsConstructor
public class JdbcTemplateUserRepositoryImpl implements UserRepository, RowMapper<User> {

    private final JdbcTemplate jdbcTemplate;
    private final TaskService taskService;

    private final String FIND_BY_USERNAME = """
            SELECT * FROM users WHERE username LIKE ?""";

    private final String FIND_BY_ID = """
            SELECT * FROM users
            WHERE id = ?""";

    private final String GET_USER_ROLES = """
            SELECT role FROM users_roles
            WHERE user_id = ?""";

    private final String CHECK_EXISTENCE_BY_ID = """
            SELECT COUNT(1) FROM users
            WHERE id = ?""";

    private final String CREATE = """
            insert into users (name, username, password)
            values (?, ?, ?)""";

    private final String UPDATE = """
            update users
            set name = ?,
            username = ?,
            password = ?""";

    private final String INSERT_USER_ROLES = """
            insert into users_roles (user_id, role)
            values (?, ?)""";

    private final String DELETE = """
            DELETE FROM users
            WHERE id = ?""";

    @Override
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
            user = jdbcTemplate.queryForObject(FIND_BY_ID, this, userId);
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User user;
        try {
            user = jdbcTemplate.queryForObject(FIND_BY_USERNAME, this, username);
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update(UPDATE, user.getName(), user.getUsername(), user.getPassword());
        return user;
    }

    @Override
    public boolean delete(Long userId) {
        return jdbcTemplate.update(DELETE, userId) != 1;
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        jdbcTemplate.update(INSERT_USER_ROLES, userId, role.name());
    }

    private Set<Role> getUserRoles(Long userId) {
        List<Role> roles = jdbcTemplate.queryForList(GET_USER_ROLES, String.class, userId).stream()
                .map(Role::valueOf)
                .toList();
        return new HashSet<>(roles);
    }

    @Override
    public boolean checkExistenceById(Long userId) {
        int count = jdbcTemplate.queryForObject(CHECK_EXISTENCE_BY_ID, Integer.class, userId);
        return count != 0;
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .build();
        user.setRoles(getUserRoles(user.getId()));
        user.setTasks(taskService.getAllByUserId(user.getId()));
        return user;
    }

}

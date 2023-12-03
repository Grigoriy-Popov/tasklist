package com.example.tasklist.repository.impl;

import com.example.tasklist.model.user.Role;
import com.example.tasklist.model.user.User;
import com.example.tasklist.repository.UserRepository;
import com.example.tasklist.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository, RowMapper<User> {

    private final JdbcTemplate jdbcTemplate;
    private final TaskService taskService;

    private final String FIND_BY_USERNAME = """
                SELECT * FROM users
                WHERE username LIKE ?""";

    private final String GET_USER_ROLES = """
                SELECT role FROM users_roles
                WHERE user_id = ?""";

    private final String CHECK_EXISTENCE_BY_ID = """
                SELECT COUNT(1) FROM users
                WHERE id = ?""";

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(FIND_BY_USERNAME, this, username));
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public boolean delete(Long userId) {
        return false;
    }

    @Override
    public void insertUserRole(Long userId, Role role) {

    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return false;
    }

    private Set<Role> getUserRoles(Long userId) {
        List<Role> roles = jdbcTemplate.queryForList(GET_USER_ROLES, String.class, userId).stream()
                .map(Role::valueOf)
                .toList();
        return new HashSet<>(roles);
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

    @Override
    public boolean checkExistenceById(Long userId) {
        int count = jdbcTemplate.queryForObject(CHECK_EXISTENCE_BY_ID, Integer.class, userId);
        return count != 0;
    }

}

package com.example.tasklist.web.security.expression;

import com.example.tasklist.model.user.Role;
import com.example.tasklist.service.TaskService;
import com.example.tasklist.service.UserService;
import com.example.tasklist.web.security.JWTEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    private final TaskService taskService;

    public boolean canAccessUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long authenticateUserId = getCurrentUserId();
        return userId.equals(authenticateUserId) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(Authentication authentication, Role... roles) {
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }

    public boolean canAccessTask(Long taskId) {
        Long authenticateUserId = getCurrentUserId();
        return taskService.getById(taskId).getUserId().equals(authenticateUserId);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTEntity user = (JWTEntity) authentication.getPrincipal();
        return user.getId();
    }

}

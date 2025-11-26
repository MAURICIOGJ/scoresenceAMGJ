package scoresense.app.mapper;

import java.util.List;
import java.util.stream.Collectors;

import scoresense.app.dto.UserRequest;
import scoresense.app.dto.UserResponse;
import scoresense.app.model.RoleEntity;
import scoresense.app.model.User;

public final class UserMapper {

    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .roleId(user.getRole() != null ? user.getRole().getRoleId() : null)
                .roleName(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    public static List<UserResponse> toResponseList(List<User> users) {
        if (users == null) {
            return List.of();
        }
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static User toEntity(UserRequest request, RoleEntity role) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(role);
        return user;
    }

    public static void copyToEntity(UserRequest request, User entity, RoleEntity role) {
        if (request == null || entity == null) {
            return;
        }

        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        if (role != null) {
            entity.setRole(role);
        }
    }
}

package scoresense.app.mapper;

import java.util.List;
import java.util.stream.Collectors;

import scoresense.app.dto.RoleRequest;
import scoresense.app.dto.RoleResponse;
import scoresense.app.model.RoleEntity;

public final class RoleMapper {

    public static RoleResponse toResponse(RoleEntity role) {
        if (role == null) {
            return null;
        }

        RoleResponse response = new RoleResponse();
        response.setRoleId(role.getRoleId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());

        return response;
    }

    public static List<RoleResponse> toResponseList(List<RoleEntity> roleList) {
        if (roleList == null) {
            return List.of();
        }
        return roleList.stream()
                .map(RoleMapper::toResponse)
                .collect(Collectors.toList());
    }

    public static RoleEntity toEntity(RoleRequest request) {
        if (request == null) {
            return null;
        }

        RoleEntity role = new RoleEntity();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        return role;
    }

    public static void copyToEntity(RoleRequest request, RoleEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
    }
}

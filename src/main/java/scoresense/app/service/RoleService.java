package scoresense.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import scoresense.app.dto.RoleRequest;
import scoresense.app.dto.RoleResponse;
import scoresense.app.exception.ResourceNotFoundException;
import scoresense.app.model.RoleEntity;
import scoresense.app.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Transactional // Applies transactions to all methods.
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public RoleResponse getRoleById(Long id) {
        RoleEntity role = roleRepository.findById(id)
                // Throws an exception if the resource is not found.
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
        return mapToResponse(role);
    }

    public RoleResponse createRole(RoleRequest request) {
        RoleEntity role = new RoleEntity();
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        RoleEntity savedRole = roleRepository.save(role);
        return mapToResponse(savedRole);
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role", "id", id);
        }
        roleRepository.deleteById(id);
    }

    private RoleResponse mapToResponse(RoleEntity role) {
        return RoleResponse.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}

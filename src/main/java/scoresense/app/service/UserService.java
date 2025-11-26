package scoresense.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import scoresense.app.dto.UserRequest;
import scoresense.app.dto.UserResponse;
import scoresense.app.exception.ResourceNotFoundException;
import scoresense.app.mapper.UserMapper;
import scoresense.app.model.RoleEntity;
import scoresense.app.model.User;
import scoresense.app.repository.RoleRepository;
import scoresense.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Loads user details required by Spring Security for authentication.
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // --- CRUD ---
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserMapper.toResponse(user);
    }

    public UserResponse create(UserRequest req) {
        RoleEntity role = null;
        if (req.getRoleId() != null) {
            role = roleRepository.findById(req.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", req.getRoleId()));
        }

        User user = UserMapper.toEntity(req, role);

        //  Hashing the password before saving it to the database.
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    public UserResponse update(Long id, UserRequest req) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        RoleEntity role = existingUser.getRole();
        if (req.getRoleId() != null) {
            role = roleRepository.findById(req.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", req.getRoleId()));
        }

        UserMapper.copyToEntity(req, existingUser, role);

        // If a new password is provided, it must be hashed and updated.
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            existingUser.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toResponse(updatedUser);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}

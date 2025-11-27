package scoresense.app.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import scoresense.app.dto.AuthRequest;
import scoresense.app.dto.AuthResponse;
import scoresense.app.dto.UserRequest;
import scoresense.app.mapper.UserMapper;
import scoresense.app.model.RoleEntity;
import scoresense.app.model.User;
import scoresense.app.repository.RoleRepository;
import scoresense.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(UserRequest request) {

        RoleEntity role = roleRepository.findById(request.getRoleId() != null ? request.getRoleId() : 2L)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = UserMapper.toEntity(request, role);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }
}

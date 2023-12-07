package uz.nazir.task.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.nazir.task.configs.jwt.JwtService;
import uz.nazir.task.dto.security.request.AuthenticationRequest;
import uz.nazir.task.dto.security.request.RegisterRequest;
import uz.nazir.task.dto.security.response.AuthenticationResponse;
import uz.nazir.task.entities.User;
import uz.nazir.task.entities.enums.Role;
import uz.nazir.task.error.exceptions.ForbiddenException;
import uz.nazir.task.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            userRepository.save(user);
        } else throw new ForbiddenException("Email is already used");

        var token = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();//TODO optional -> implement throwing exception

        var token = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .token(token)
                .build();
    }
}

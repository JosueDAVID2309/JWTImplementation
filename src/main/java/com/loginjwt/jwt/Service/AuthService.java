package com.loginjwt.jwt.Service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.loginjwt.jwt.DTO.LoginRequest;
import com.loginjwt.jwt.DTO.RegisterRequest;
import com.loginjwt.jwt.Model.User;
import com.loginjwt.jwt.Repository.UserRepository;

@Service
public class AuthService {
    private final PasswordEncoder encoder;
    private final UserRepository repoUser;

    private final JwtService jwt;

    public AuthService(PasswordEncoder encoder, UserRepository repoUser, JwtService jwt) {
        this.encoder = encoder;
        this.repoUser = repoUser;
        this.jwt = jwt;
    }

    public String login(LoginRequest request) {
        User user = repoUser.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales Incorrectas"));
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales Incorrectas");
        }
        return jwt.generateToken(user.getEmail());
    }

    public String register(RegisterRequest request){
        if(repoUser.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email ya existe");
        }

        User user = new User();
        user.setNombres(request.getNombre());
        user.setApellidos(request.getApellido());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        repoUser.save(user);
        return jwt.generateToken(user.getEmail());

    }

}

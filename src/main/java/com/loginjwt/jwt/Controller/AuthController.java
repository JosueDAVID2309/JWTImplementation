package com.loginjwt.jwt.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loginjwt.jwt.DTO.ApiResponse;
import com.loginjwt.jwt.DTO.LoginRequest;
import com.loginjwt.jwt.DTO.RegisterRequest;
import com.loginjwt.jwt.Service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {
        try {
            String token = auth.login(request);
            return ResponseEntity.ok(
                    new ApiResponse<>(true,token,"Inicio de sesión exitoso"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false,null,"Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request) { 
        try{
            String token = auth.register(request);
            return ResponseEntity.ok(
                    new ApiResponse<>(true,token,"Registro exitoso"));
        }catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false,null,"Credenciales inválidas"));
        }
    }
}

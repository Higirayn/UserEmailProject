package org.example.security.controller;

import lombok.RequiredArgsConstructor;
import org.example.security.model.AuthRequest;
import org.example.security.model.AuthResponse;
import org.example.security.model.RegisterRequest;
import org.example.security.model.ValidateTokenRequest;
import org.example.security.model.ValidateTokenResponse;
import org.example.security.service.AuthService;
import org.example.security.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService;
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
    
    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponse> validateToken(@RequestBody ValidateTokenRequest request) {
        try {
            String username = jwtService.extractUsername(request.getToken());
            boolean isValid = jwtService.isTokenValid(request.getToken(), 
                authService.loadUserByUsername(username));
            
            return ResponseEntity.ok(new ValidateTokenResponse(isValid, username));
        } catch (Exception e) {
            return ResponseEntity.ok(new ValidateTokenResponse(false, null));
        }
    }
    
    @GetMapping("/public/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Security service is running!");
    }
} 
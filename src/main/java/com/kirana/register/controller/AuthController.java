package com.kirana.register.controller;

import com.kirana.register.dto.SignupRequest;
import com.kirana.register.dto.SigninRequest;
import com.kirana.register.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import com.kirana.register.repository.UserRepository;
import com.kirana.register.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    

    @Autowired
    private JwtUtil jwtUtil;

    

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Password and confirm password do not match");
        }
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        if (userRepository.findByUsername(request.getUsername()) != null) {

            return ResponseEntity.badRequest().body("Username already taken");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), hashedPassword, request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole().toUpperCase() : "USER");
        userRepository.save(user);
        return ResponseEntity.ok("Signup successful");
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SigninRequest request) {
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(token);
    }
    

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public ResponseEntity<String> viewAdminDashboard() {
        return ResponseEntity.ok("Welcome Admin");
    }

    
    
}

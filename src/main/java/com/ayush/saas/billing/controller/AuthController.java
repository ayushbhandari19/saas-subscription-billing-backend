package com.ayush.saas.billing.controller;

import com.ayush.saas.billing.model.User;
import com.ayush.saas.billing.repository.UserRepository;
import com.ayush.saas.billing.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwt;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepo.save(user);
        return "Registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody User req) {
        User user = userRepo.findByEmail(req.getEmail()).orElseThrow();
        if (!encoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        return jwt.generateToken(user.getEmail());
    }
}

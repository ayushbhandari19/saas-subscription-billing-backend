package com.ayush.saas.billing.controller;

import com.ayush.saas.billing.model.User;
import com.ayush.saas.billing.repository.UserRepository;
import com.ayush.saas.billing.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import com.stripe.model.Customer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwt;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest req) throws Exception {

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setRole("USER");

        Map<String, Object> params = new HashMap<>();
        params.put("email", req.getEmail());
        Customer customer = Customer.create(params);

        user.setStripeCustomerId(customer.getId());

        return userRepo.save(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {

        String email = req.get("email");
        String password = req.get("password");

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwt.generateToken(user.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return response;
    }

}

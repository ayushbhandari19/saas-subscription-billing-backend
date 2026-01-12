package com.ayush.saas.billing.controller;

import com.ayush.saas.billing.model.User;
import com.ayush.saas.billing.repository.UserRepository;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {

    private final UserRepository userRepo;

    @PostMapping("/create-checkout-session")
    public String createSession(@RequestParam String email,
                                @RequestParam String priceId) throws Exception {

        User user = userRepo.findByEmail(email).orElseThrow();

        if (user.getStripeCustomerId() == null) {
            Customer customer = Customer.create(Map.of("email", user.getEmail()));
            user.setStripeCustomerId(customer.getId());
            userRepo.save(user);
        }

        Session session = Session.create(
                Map.of(
                        "mode", "subscription",
                        "customer", user.getStripeCustomerId(),
                        "line_items", List.of(
                                Map.of("price", priceId, "quantity", 1)
                        ),
                        "success_url", "http://localhost:3000/success",
                        "cancel_url", "http://localhost:3000/cancel"
                )
        );

        return session.getUrl();
    }
}

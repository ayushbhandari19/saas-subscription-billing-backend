package com.ayush.saas.billing.controller;

import com.ayush.saas.billing.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionRepository subscriptionRepo;

    @GetMapping("/{userId}")
    public Object getUserSubscription(@PathVariable Long userId) {
        return subscriptionRepo.findByUserId(userId);
    }
}

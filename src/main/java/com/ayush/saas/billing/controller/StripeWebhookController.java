package com.ayush.saas.billing.controller;

import com.ayush.saas.billing.model.Subscription;
import com.ayush.saas.billing.repository.SubscriptionRepository;
import com.stripe.model.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final SubscriptionRepository subscriptionRepo;

    @PostMapping("/stripe")
    public String handleStripe(@RequestBody Event event) {

        if (event.getType().startsWith("customer.subscription")) {
            com.stripe.model.Subscription stripeSub =
                    (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                            .getObject().orElse(null);

            if (stripeSub != null) {
                Subscription sub = subscriptionRepo
                        .findByStripeSubscriptionId(stripeSub.getId())
                        .orElse(new Subscription());

                sub.setStripeSubscriptionId(stripeSub.getId());
                sub.setStatus(stripeSub.getStatus());
                sub.setCurrentPeriodEnd(
                        Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd())
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDateTime()
                );

                subscriptionRepo.save(sub);
            }
        }

        return "OK";
    }
}

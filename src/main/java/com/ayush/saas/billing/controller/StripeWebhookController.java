package com.ayush.saas.billing.controller;

import com.ayush.saas.billing.model.Subscription;
import com.ayush.saas.billing.repository.SubscriptionRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final SubscriptionRepository subscriptionRepo;

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    @PostMapping("/stripe")
    public String handleStripe(@RequestBody String payload,
                               @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if (event.getType().startsWith("customer.subscription")) {
                com.stripe.model.Subscription stripeSub =
                        (com.stripe.model.Subscription) event.getDataObjectDeserializer()
                                .getObject().orElse(null);

                Subscription sub = subscriptionRepo
                        .findByStripeSubscriptionId(stripeSub.getId())
                        .orElse(new Subscription());

                sub.setStripeSubscriptionId(stripeSub.getId());
                sub.setStatus(stripeSub.getStatus());
                sub.setCurrentPeriodEnd(
                        Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
                );

                subscriptionRepo.save(sub);
            }

            return "OK";

        } catch (SignatureVerificationException e) {
            return "Invalid signature";
        }
    }
}

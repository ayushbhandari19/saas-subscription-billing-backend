package com.ayush.saas.billing.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    @PostMapping("/stripe")
    public String handleStripe(@RequestBody String payload,
                               @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if (event.getType().equals("customer.subscription.created")) {
                Subscription sub = (Subscription) event.getDataObjectDeserializer()
                        .getObject().orElse(null);

                System.out.println("New subscription: " + sub.getId());
            }

            if (event.getType().equals("customer.subscription.deleted")) {
                System.out.println("Subscription canceled");
            }

            return "OK";

        } catch (SignatureVerificationException e) {
            return "Invalid signature";
        }
    }
}

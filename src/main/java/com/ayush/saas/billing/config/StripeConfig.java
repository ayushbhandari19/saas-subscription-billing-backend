package com.ayush.saas.billing.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {

    @PostConstruct
    public void init() {
        Stripe.apiKey = "REMOVED51SooEnB04rw3QJ8QnuxJvSMIqazIZAAJ9BBDCx535sC6glYO7nktx46TMl9STPvP6aO3GArBPuzfv3Y2DC4c6pZj00IBZM7guH";
    }
}

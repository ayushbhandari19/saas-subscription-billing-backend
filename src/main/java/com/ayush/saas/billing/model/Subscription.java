package com.ayush.saas.billing.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String stripeSubscriptionId;
    private String status;
    private LocalDateTime currentPeriodEnd;
}

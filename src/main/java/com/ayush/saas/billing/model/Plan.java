package com.ayush.saas.billing.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String stripePriceId;
    private Double price;
}

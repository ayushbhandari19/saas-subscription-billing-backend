package com.ayush.saas.billing.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")   // 👈 THIS FIXES THE CRASH
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String role;

    private String stripeCustomerId;
}

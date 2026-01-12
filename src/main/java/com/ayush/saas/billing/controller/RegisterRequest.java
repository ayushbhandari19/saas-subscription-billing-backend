package com.ayush.saas.billing.controller;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}

package com.kyalo.banksimple;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password"; // Your raw password
        String hashedPassword = encoder.encode(rawPassword); // Hash the password
        System.out.println("Hashed Password: " + hashedPassword); // Store this value
    }
}

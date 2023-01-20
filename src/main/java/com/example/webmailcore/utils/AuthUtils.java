package com.example.webmailcore.utils;

import com.example.webmailcore.auth.CustomUserDetails;
import com.example.webmailcore.models.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Object principal = SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                String userId = ((CustomUserDetails) principal).getUserId();
                User user = new User();
                user.setId(userId);
                return user;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
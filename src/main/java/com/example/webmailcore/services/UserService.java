package com.example.webmailcore.services;

import com.example.webmailcore.auth.CustomUserDetails;
import com.example.webmailcore.models.User;
import com.example.webmailcore.repositories.UserRepository;
import com.example.webmailcore.repositories.specifications.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public Page<User> all(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<User> allAirplaneCompanyMembers(String airplaneCompanyId) {
        return repository.findAllByAirplaneCompany_Id(airplaneCompanyId);
    }

    public Page<User> all(Map<String, Object> params, Pageable pageable) {
        UserSpecification userSpecification = new UserSpecification();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        return repository.findAll(userSpecification, pageable);
    }

    public User get(String id) {
        User user = repository.getById(id);
        return user;
    }

    public User getUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public User save(User user) {
        User savedUser = repository.save(user);
        return savedUser;
    }


    public CustomUserDetails getCurrentUserDetails() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Object principal = SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {
            Object principal = SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (principal instanceof CustomUserDetails) {
                String userId = ((CustomUserDetails) principal).getUserId();
                return get(userId);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}

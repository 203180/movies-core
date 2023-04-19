package com.example.webmailcore.auth.controllers;

import com.example.webmailcore.auth.CustomUserDetails;
import com.example.webmailcore.auth.JwtUtil;
import com.example.webmailcore.auth.MyUserDetailsService;
import com.example.webmailcore.auth.models.AuthenticationRequest;
import com.example.webmailcore.auth.models.AuthenticationResponse;
import com.example.webmailcore.models.User;
import com.example.webmailcore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
            try {
            User user = userService.getUserByUsername(authenticationRequest.getUsername());
            if (user == null) {
                return ResponseEntity.badRequest().body("Your username or password is incorrect.");
            } else if (user.getIsEnabled() == null || !user.getIsEnabled()) {
                return ResponseEntity.badRequest().body("User is disabled");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        final CustomUserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(value = "/validateToken", method = RequestMethod.GET)
    public Boolean validateToken(@RequestParam(value = "token") String token, @RequestParam(value = "username") String username) {
        final CustomUserDetails userDetails = userDetailsService
                .loadUserByUsername(username);
        return jwtTokenUtil.validateToken(token, userDetails);
    }
}

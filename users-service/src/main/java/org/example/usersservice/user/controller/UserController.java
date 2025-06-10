package org.example.usersservice.user.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.usersservice.notification.email.EmailNotification;
import org.example.usersservice.user.dto.ChangePasswordRequest;
import org.example.usersservice.user.security.JwtUtil;
import org.example.usersservice.user.dto.LoginRequest;
import org.example.usersservice.user.service.UserService;
import org.example.usersservice.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final EmailNotification emailNotification;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public User register(@RequestBody User user) {

        User newUser = userService.registerUser(user);
        emailNotification.sendEmail(user.getEmail(),"rejestracja");
        return newUser;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok().body(Map.of("token", token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


    @GetMapping("/me")
    public User getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.extractClaims(jwt);
                String username = claims.getSubject();

                if (username != null) {
                    User user = userService.findByUsername(username);
                    if (user != null) {
                        return user;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Błąd autoryzacji: " + e.getMessage());
            }
        }

        throw new RuntimeException("User not authenticated");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser, HttpServletRequest request) {
        String username = jwtUtil.extractUsernameFromRequest(request);
        User existingUser = userService.findByUsername(username);

        if (existingUser != null) {
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setUsername(updatedUser.getUsername());

            userService.save(existingUser);
            return ResponseEntity.ok(existingUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpServletRequest request) {
        String username = jwtUtil.extractUsernameFromRequest(request);
        User user = userService.findByUsername(username);

        if (user != null) {
            userService.delete(user);
            return ResponseEntity.ok("Account deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest requestBody, HttpServletRequest request) {
        String username = jwtUtil.extractUsernameFromRequest(request);
        User user = userService.findByUsername(username);

        if (user != null && passwordEncoder.matches(requestBody.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(requestBody.getNewPassword()));
            userService.save(user);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        User user = userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Generuj tymczasowy token JWT (na np. 10 minut)
        String resetToken = jwtUtil.generateTokenWithExpiry(username, 10); // 10 minut

        // Wysyłka wiadomości z linkiem resetującym hasło
        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken;
        emailNotification.sendEmail(user.getEmail(), "Kliknij aby zresetować hasło: " + resetLink);

        return ResponseEntity.ok("Reset link sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");

        try {
            String username = jwtUtil.extractUsername(token);
            User user = userService.findByUsername(username);

            if (user != null) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userService.save(user);
                return ResponseEntity.ok("Password has been reset.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password reset failed.");
    }
}

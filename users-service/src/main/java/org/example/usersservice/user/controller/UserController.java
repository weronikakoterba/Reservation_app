package org.example.usersservice.user.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.example.usersservice.user.dto.ChangePasswordRequest;
import org.example.usersservice.user.security.JwtUtil;
import org.example.usersservice.user.dto.LoginRequest;
import org.example.usersservice.user.service.UserService;
import org.example.usersservice.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
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

        if (user != null && user.getPassword().equals(requestBody.getOldPassword())) {
            user.setPassword(requestBody.getNewPassword());
            userService.save(user);
            return ResponseEntity.ok("Password changed successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "hej coś";
    }

}

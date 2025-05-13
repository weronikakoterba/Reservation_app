package org.example.usersservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;

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
}

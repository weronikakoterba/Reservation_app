package org.example.usersservice;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private JwtUtil jwtUtil = new JwtUtil();

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        if (user.getUsername().equals("admin") && user.getPassword().equals("password")) {
            return jwtUtil.generateToken(user.getUsername());
        }
        return "Invalid credentials";
    }
}

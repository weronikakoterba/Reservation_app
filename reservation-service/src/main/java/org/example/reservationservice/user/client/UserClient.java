package org.example.reservationservice.user.client;

import lombok.RequiredArgsConstructor;
import org.example.reservationservice.user.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    public Long getUserIdByUsername(String username) {
        String url = "http://users-service.reservation-app/users/username/" + username;
        UserDTO user = restTemplate.getForObject(url, UserDTO.class);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return user.getId();
    }
}

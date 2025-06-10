package org.example.reservationservice.reservation.controller;

import org.example.reservationservice.reservation.dto.ServiceDTO;
import org.example.reservationservice.reservation.model.ServiceType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceController {

    @GetMapping
    public List<ServiceDTO> getServices() {
        return Arrays.stream(ServiceType.values())
                .map(s -> new ServiceDTO(s.name(), s.getDescription(), s.getDurationInMinutes(), s.getPrice()))
                .collect(Collectors.toList());
    }
}

package com.example.TpDA1.service;

import com.example.TpDA1.dto.IncidentDto;
import com.example.TpDA1.dto.ReportIncidentDto;
import com.example.TpDA1.model.Incident;
import com.example.TpDA1.model.Route;
import com.example.TpDA1.model.User;
import com.example.TpDA1.repository.IncidentRepository;
import com.example.TpDA1.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository incidentRepository;
    private final RouteRepository routeRepository;

    public Incident reportIncident(ReportIncidentDto reportDto, User driver) {
        Route route = routeRepository.findById(reportDto.getRouteId())
                .orElseThrow(() -> new RuntimeException("Route not found"));

        // Verify that the route belongs to the current driver
        if (route.getDriver() == null || !route.getDriver().getId().equals(driver.getId())) {
            throw new RuntimeException("This route is not assigned to you");
        }

        // Create the incident
        Incident incident = Incident.builder()
                .route(route)
                .type(reportDto.getType())
                .description(reportDto.getDescription())
                .photoUrl(reportDto.getPhotoUrl())
                .build();

        // Mark the route as completed
        route.setStatus("COMPLETED");
        routeRepository.save(route);

        // Save and return the incident
        return incidentRepository.save(incident);
    }

    public List<Incident> getIncidentsForRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found"));
        return incidentRepository.findByRoute(route);
    }

    public IncidentDto convertToDto(Incident incident) {
        return IncidentDto.builder()
                .id(incident.getId())
                .type(incident.getType())
                .description(incident.getDescription())
                .photoUrl(incident.getPhotoUrl())
                .reportedAt(incident.getReportedAt())
                .build();
    }
}

package com.example.TpDA1.controller;

import com.example.TpDA1.dto.IncidentDto;
import com.example.TpDA1.dto.ReportIncidentDto;
import com.example.TpDA1.model.Incident;
import com.example.TpDA1.model.User;
import com.example.TpDA1.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService incidentService;

    @PostMapping("/report")
    public ResponseEntity<IncidentDto> reportIncident(
            @RequestBody ReportIncidentDto reportDto,
            @AuthenticationPrincipal User driver) {

        Incident incident = incidentService.reportIncident(reportDto, driver);
        return ResponseEntity.ok(incidentService.convertToDto(incident));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<IncidentDto>> getIncidentsForRoute(
            @PathVariable Long routeId,
            @AuthenticationPrincipal User user) {

        List<Incident> incidents = incidentService.getIncidentsForRoute(routeId);
        List<IncidentDto> incidentDtos = incidents.stream()
                .map(incidentService::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(incidentDtos);
    }
}

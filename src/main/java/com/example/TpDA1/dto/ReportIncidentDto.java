package com.example.TpDA1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportIncidentDto {
    private Long routeId;
    private String type; // WRONG_ADDRESS, CUSTOMER_ABSENT, DAMAGED_PACKAGE, OTHER
    private String description;
    private String photoUrl; // Base64 encoded string or URL
}

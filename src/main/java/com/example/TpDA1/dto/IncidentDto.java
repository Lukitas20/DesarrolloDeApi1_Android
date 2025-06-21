package com.example.TpDA1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDto {
    private Long id;
    private String type;
    private String description;
    private String photoUrl;
    private LocalDateTime reportedAt;
}

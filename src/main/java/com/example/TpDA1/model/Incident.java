package com.example.TpDA1.model;

Me import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    @JsonBackReference // This prevents infinite recursion in JSON serialization
    private Route route;

    @Column(nullable = false)
    private String type; // WRONG_ADDRESS, CUSTOMER_ABSENT, DAMAGED_PACKAGE, OTHER

    @Column(nullable = false)
    private String description;

    @Column(name = "photo_url")
    private String photoUrl; // URL to the stored photo (optional)

    @Column(name = "reported_at", nullable = false)
    private LocalDateTime reportedAt;

    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
    }
}

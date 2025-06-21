package com.example.TpDA1.repository;

import com.example.TpDA1.model.Incident;
import com.example.TpDA1.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByRoute(Route route);
}

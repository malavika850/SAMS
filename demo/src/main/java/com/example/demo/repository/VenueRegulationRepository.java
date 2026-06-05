package com.example.demo.repository;

import com.example.demo.model.VenueRegulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VenueRegulationRepository extends JpaRepository<VenueRegulation, Long> {
    List<VenueRegulation> findByVenueId(Long venueId);
}
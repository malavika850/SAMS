package com.example.demo.repository;

import com.example.demo.model.VenueImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VenueImagesRepository extends JpaRepository<VenueImages, Long> {
    List<VenueImages> findByVenueId(Long venueId);
    int countByVenueId(Long venueId);
}
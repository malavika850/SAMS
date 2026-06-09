package com.example.demo.service;

import com.example.demo.model.Venue;
import com.example.demo.model.VenueImages;
import com.example.demo.repository.VenueImagesRepository;
import com.example.demo.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VenueImagesService {

    @Autowired
    private VenueImagesRepository venueImagesRepository;

    @Autowired
    private VenueRepository venueRepository;

    public List<VenueImages> getImagesByVenue(Long venueId) {
        return venueImagesRepository.findByVenueId(venueId);
    }

    public VenueImages addImage(VenueImages image) {
        Long venueId = image.getVenue().getId();

        // Max 3 images per venue
        int count = venueImagesRepository.countByVenueId(venueId);
        if (count >= 3) {
            throw new RuntimeException("Maximum 3 images allowed per venue");
        }

        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new RuntimeException("Venue not found"));
        image.setVenue(venue);

        return venueImagesRepository.save(image);
    }

    public void deleteImage(Long id) {
        venueImagesRepository.deleteById(id);
    }
}
package com.example.demo.service;

import com.example.demo.model.VenueRegulation;
import com.example.demo.repository.VenueRegulationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VenueRegulationService {

    @Autowired
    private VenueRegulationRepository venueRegulationRepository;

    public List<VenueRegulation> getAllRegulations() {
        return venueRegulationRepository.findAll();
    }

    public VenueRegulation createRegulation(VenueRegulation regulation) {
        return venueRegulationRepository.save(regulation);
    }

    public VenueRegulation getRegulationById(Long id) {
        return venueRegulationRepository.findById(id).orElse(null);
    }

    public void deleteRegulation(Long id) {
        venueRegulationRepository.deleteById(id);
    }
}
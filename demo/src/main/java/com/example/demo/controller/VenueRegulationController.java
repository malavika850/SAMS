package com.example.demo.controller;

import com.example.demo.model.VenueRegulation;
import com.example.demo.service.VenueRegulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/regulations")
public class VenueRegulationController {

    @Autowired
    private VenueRegulationService venueRegulationService;

    @GetMapping
    public List<VenueRegulation> getAllRegulations() {
        return venueRegulationService.getAllRegulations();
    }

    @PostMapping
    public VenueRegulation createRegulation(@RequestBody VenueRegulation regulation) {
        return venueRegulationService.createRegulation(regulation);
    }

    @GetMapping("/{id}")
    public VenueRegulation getRegulationById(@PathVariable Long id) {
        return venueRegulationService.getRegulationById(id);
    }

    @PutMapping("/{id}")
    public VenueRegulation updateRegulation(@PathVariable Long id, @RequestBody VenueRegulation regulation) {
        VenueRegulation existing = venueRegulationService.getRegulationById(id);
        existing.setName(regulation.getName());
        existing.setStartTime(regulation.getStartTime());
        existing.setEndTime(regulation.getEndTime());
        existing.setRules(regulation.getRules());
        existing.setVenue(regulation.getVenue());
        return venueRegulationService.createRegulation(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteRegulation(@PathVariable Long id) {
        venueRegulationService.deleteRegulation(id);
        return "Regulation deleted successfully";
    }
}
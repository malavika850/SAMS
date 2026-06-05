package com.example.demo.controller;

import com.example.demo.model.Venue;
import com.example.demo.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @GetMapping
    public List<Venue> getAllVenues() {
        return venueService.getAllVenues();
    }

    @PostMapping
    public Venue createVenue(@RequestBody Venue venue) {
        return venueService.createVenue(venue);
    }

    @GetMapping("/{id}")
    public Venue getVenueById(@PathVariable Long id) {
        return venueService.getVenueById(id);
    }
    @PutMapping("/{id}")
    public Venue updateVenue(@PathVariable Long id, @RequestBody Venue venue) {
    Venue existing = venueService.getVenueById(id);
    existing.setName(venue.getName());
    existing.setLocation(venue.getLocation());
    existing.setPrice(venue.getPrice());
    existing.setActiveStatus(venue.getActiveStatus());
    existing.setBookingStatus(venue.getBookingStatus());
    existing.setOwner(venue.getOwner());
    return venueService.createVenue(existing);
}

    @DeleteMapping("/{id}")
    public String deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
        return "Venue deleted successfully";
    }
}
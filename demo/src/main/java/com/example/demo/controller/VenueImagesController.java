package com.example.demo.controller;

import com.example.demo.model.VenueImages;
import com.example.demo.service.VenueImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/venue-images")
public class VenueImagesController {

    @Autowired
    private VenueImagesService venueImagesService;

    @GetMapping("/venue/{venueId}")
    public List<VenueImages> getImagesByVenue(@PathVariable Long venueId) {
        return venueImagesService.getImagesByVenue(venueId);
    }

    @PostMapping
    public VenueImages addImage(@RequestBody VenueImages image) {
        return venueImagesService.addImage(image);
    }

    @DeleteMapping("/{id}")
    public String deleteImage(@PathVariable Long id) {
        venueImagesService.deleteImage(id);
        return "Image deleted successfully";
    }
}
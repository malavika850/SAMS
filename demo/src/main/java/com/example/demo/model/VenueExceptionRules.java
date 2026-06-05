package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "venue_exception_rules")
public class VenueExceptionRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalTime unavailableStartTime;
    private LocalTime unavailableEndTime;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalTime getUnavailableStartTime() { return unavailableStartTime; }
    public void setUnavailableStartTime(LocalTime unavailableStartTime) { this.unavailableStartTime = unavailableStartTime; }
    public LocalTime getUnavailableEndTime() { return unavailableEndTime; }
    public void setUnavailableEndTime(LocalTime unavailableEndTime) { this.unavailableEndTime = unavailableEndTime; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
}
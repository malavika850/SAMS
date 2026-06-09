package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "venue_exception_rules")
public class VenueExceptionRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate exceptionDate;

    private LocalTime unavailableStartTime;
    private LocalTime unavailableEndTime;

    @Enumerated(EnumType.STRING)
    private VenueExceptionType venueExceptionType;

    @Enumerated(EnumType.STRING)
    private ExceptionStatus exceptionStatus;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public enum VenueExceptionType {
        MAINTENANCE, HOLIDAY, TEMPORARY_UNAVAILABLE
    }

    public enum ExceptionStatus {
        UPDATED, CONFLICTED
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getExceptionDate() { return exceptionDate; }
    public void setExceptionDate(LocalDate exceptionDate) { this.exceptionDate = exceptionDate; }
    public LocalTime getUnavailableStartTime() { return unavailableStartTime; }
    public void setUnavailableStartTime(LocalTime unavailableStartTime) { this.unavailableStartTime = unavailableStartTime; }
    public LocalTime getUnavailableEndTime() { return unavailableEndTime; }
    public void setUnavailableEndTime(LocalTime unavailableEndTime) { this.unavailableEndTime = unavailableEndTime; }
    public VenueExceptionType getVenueExceptionType() { return venueExceptionType; }
    public void setVenueExceptionType(VenueExceptionType venueExceptionType) { this.venueExceptionType = venueExceptionType; }
    public ExceptionStatus getExceptionStatus() { return exceptionStatus; }
    public void setExceptionStatus(ExceptionStatus exceptionStatus) { this.exceptionStatus = exceptionStatus; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
}
package com.example.demo.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "venue_regulations")
public class VenueRegulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DayOfWeek weekStartDay;

    @Enumerated(EnumType.STRING)
    private DayOfWeek weekEndDay;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer minDuration;
    private Integer bookBefore;
    private boolean isCurrentlyActive;

    private String rules;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DayOfWeek getWeekStartDay() { return weekStartDay; }
    public void setWeekStartDay(DayOfWeek weekStartDay) { this.weekStartDay = weekStartDay; }
    public DayOfWeek getWeekEndDay() { return weekEndDay; }
    public void setWeekEndDay(DayOfWeek weekEndDay) { this.weekEndDay = weekEndDay; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public Integer getMinDuration() { return minDuration; }
    public void setMinDuration(Integer minDuration) { this.minDuration = minDuration; }
    public Integer getBookBefore() { return bookBefore; }
    public void setBookBefore(Integer bookBefore) { this.bookBefore = bookBefore; }
    public boolean isCurrentlyActive() { return isCurrentlyActive; }
    public void setCurrentlyActive(boolean isCurrentlyActive) { this.isCurrentlyActive = isCurrentlyActive; }
    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
}
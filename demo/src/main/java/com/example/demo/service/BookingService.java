package com.example.demo.service;

import com.example.demo.model.Booking;
import com.example.demo.model.Venue;
import com.example.demo.model.VenueExceptionRules;
import com.example.demo.model.VenueRegulation;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.VenueExceptionRulesRepository;
import com.example.demo.repository.VenueRegulationRepository;
import com.example.demo.repository.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private VenueRegulationRepository venueRegulationRepository;

    @Autowired
    private VenueExceptionRulesRepository venueExceptionRulesRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getBookingsByVenue(Long venueId) {
        return bookingRepository.findByVenueId(venueId);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking createBooking(Booking booking) {
        Venue venue = venueRepository.findById(booking.getVenue().getId())
                .orElseThrow(() -> new RuntimeException("Venue not found"));

        // Check 1 — venue is active
        if (!venue.getActiveStatus()) {
            throw new RuntimeException("Venue is not active");
        }

        // Check 2 — venue accepts bookings
        if (!venue.getBookingStatus()) {
            throw new RuntimeException("Venue is not accepting bookings");
        }

        // Check 3 — booking is within regulation hours
        List<VenueRegulation> regulations = venueRegulationRepository.findByVenueId(venue.getId());
        if (!regulations.isEmpty()) {
            boolean withinHours = regulations.stream().anyMatch(reg ->
                !booking.getStartTime().isBefore(reg.getStartTime()) &&
                !booking.getEndTime().isAfter(reg.getEndTime())
            );
            if (!withinHours) {
                throw new RuntimeException("Booking time is outside venue operating hours");
            }
        }

        // Check 4 — not in exception rules (unavailable times)
        List<VenueExceptionRules> exceptions = venueExceptionRulesRepository.findByVenueId(venue.getId());
        for (VenueExceptionRules ex : exceptions) {
            if (booking.getStartTime().isBefore(ex.getUnavailableEndTime()) &&
                booking.getEndTime().isAfter(ex.getUnavailableStartTime())) {
                throw new RuntimeException("Venue is unavailable during this time: " + ex.getName());
            }
        }

        // Check 5 — no overlapping bookings
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
            venue.getId(),
            booking.getDate(),
            booking.getStartTime(),
            booking.getEndTime()
        );
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("This time slot is already booked");
        }

        // Calculate duration automatically
        int duration = booking.getEndTime().getHour() - booking.getStartTime().getHour();
        booking.setDuration(duration);
        booking.setStatus(Booking.Status.PENDING);

        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(Long id, Booking.Status status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
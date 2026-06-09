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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    public List<String> getAvailableSlots(Long venueId, LocalDate date, int durationHours) {
        List<VenueRegulation> regulations = venueRegulationRepository.findByVenueId(venueId);
        if (regulations.isEmpty()) {
            throw new RuntimeException("No regulations set for this venue");
        }

        List<VenueExceptionRules> exceptions = venueExceptionRulesRepository.findByVenueId(venueId);
        List<Booking> existingBookings = bookingRepository.findByVenueIdAndDate(venueId, date);
        List<String> availableSlots = new ArrayList<>();

        for (VenueRegulation reg : regulations) {
            LocalTime current = reg.getStartTime();
            LocalTime venueClose = reg.getEndTime();

            while (!current.plusHours(durationHours).isAfter(venueClose)) {
                LocalTime slotEnd = current.plusHours(durationHours);
                final LocalTime slotStart = current;

                boolean inException = exceptions.stream().anyMatch(ex ->
                    ex.getUnavailableStartTime() != null &&
                    ex.getUnavailableEndTime() != null &&
                    slotStart.isBefore(ex.getUnavailableEndTime()) &&
                    slotEnd.isAfter(ex.getUnavailableStartTime())
                );

                boolean alreadyBooked = existingBookings.stream().anyMatch(b ->
                    b.getStatus() != Booking.Status.CANCELLED &&
                    slotStart.isBefore(b.getEndTime()) &&
                    slotEnd.isAfter(b.getStartTime())
                );

                if (!inException && !alreadyBooked) {
                    availableSlots.add(slotStart + " - " + slotEnd);
                }

                current = current.plusHours(1);
            }
        }

        return availableSlots;
    }

    // Check if venue has any slots left today
    private boolean hasAvailableSlotsToday(Long venueId) {
        LocalDate today = LocalDate.now();
        List<VenueRegulation> regulations = venueRegulationRepository.findByVenueId(venueId);
        if (regulations.isEmpty()) return false;

        List<VenueExceptionRules> exceptions = venueExceptionRulesRepository.findByVenueId(venueId);
        List<Booking> existingBookings = bookingRepository.findByVenueIdAndDate(venueId, today);

        for (VenueRegulation reg : regulations) {
            LocalTime current = reg.getStartTime();
            LocalTime venueClose = reg.getEndTime();

            while (!current.plusHours(1).isAfter(venueClose)) {
                LocalTime slotEnd = current.plusHours(1);
                final LocalTime slotStart = current;

                boolean inException = exceptions.stream().anyMatch(ex ->
                    ex.getUnavailableStartTime() != null &&
                    ex.getUnavailableEndTime() != null &&
                    slotStart.isBefore(ex.getUnavailableEndTime()) &&
                    slotEnd.isAfter(ex.getUnavailableStartTime())
                );

                boolean alreadyBooked = existingBookings.stream().anyMatch(b ->
                    b.getStatus() != Booking.Status.CANCELLED &&
                    slotStart.isBefore(b.getEndTime()) &&
                    slotEnd.isAfter(b.getStartTime())
                );

                if (!inException && !alreadyBooked) {
                    return true; // Found at least one available slot
                }

                current = current.plusHours(1);
            }
        }

        return false; // No slots available
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

        // Check 4 — not in exception rules
        List<VenueExceptionRules> exceptions = venueExceptionRulesRepository.findByVenueId(venue.getId());
        for (VenueExceptionRules ex : exceptions) {
            if (ex.getUnavailableStartTime() != null &&
                ex.getUnavailableEndTime() != null &&
                booking.getStartTime().isBefore(ex.getUnavailableEndTime()) &&
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

        // Auto confirm since all checks passed
        booking.setStatus(Booking.Status.CONFIRMED);

        Booking saved = bookingRepository.save(booking);

        // Auto-close venue if no slots left today
        if (!hasAvailableSlotsToday(venue.getId())) {
            venue.setBookingStatus(false);
            venueRepository.save(venue);
            System.out.println(">>> Venue " + venue.getName() + " booking status set to FALSE - fully booked!");
        }

        return saved;
    }

    public Booking updateBookingStatus(Long id, Booking.Status status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // If booking is cancelled, re-open venue bookings
        if (status == Booking.Status.CANCELLED) {
            Venue venue = booking.getVenue();
            venue.setBookingStatus(true);
            venueRepository.save(venue);
            System.out.println(">>> Venue " + venue.getName() + " booking status set to TRUE - slot freed!");
        }

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
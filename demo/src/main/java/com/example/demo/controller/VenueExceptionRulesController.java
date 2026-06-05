package com.example.demo.controller;

import com.example.demo.model.VenueExceptionRules;
import com.example.demo.service.VenueExceptionRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/exception-rules")
public class VenueExceptionRulesController {

    @Autowired
    private VenueExceptionRulesService venueExceptionRulesService;

    @GetMapping
    public List<VenueExceptionRules> getAllExceptionRules() {
        return venueExceptionRulesService.getAllExceptionRules();
    }

    @PostMapping
    public VenueExceptionRules createExceptionRule(@RequestBody VenueExceptionRules exceptionRule) {
        return venueExceptionRulesService.createExceptionRule(exceptionRule);
    }

    @GetMapping("/{id}")
    public VenueExceptionRules getExceptionRuleById(@PathVariable Long id) {
        return venueExceptionRulesService.getExceptionRuleById(id);
    }

    @PutMapping("/{id}")
    public VenueExceptionRules updateExceptionRule(@PathVariable Long id, @RequestBody VenueExceptionRules exceptionRule) {
        VenueExceptionRules existing = venueExceptionRulesService.getExceptionRuleById(id);
        existing.setName(exceptionRule.getName());
        existing.setUnavailableStartTime(exceptionRule.getUnavailableStartTime());
        existing.setUnavailableEndTime(exceptionRule.getUnavailableEndTime());
        existing.setVenue(exceptionRule.getVenue());
        return venueExceptionRulesService.createExceptionRule(existing);
    }

    @DeleteMapping("/{id}")
    public String deleteExceptionRule(@PathVariable Long id) {
        venueExceptionRulesService.deleteExceptionRule(id);
        return "Exception rule deleted successfully";
    }
}
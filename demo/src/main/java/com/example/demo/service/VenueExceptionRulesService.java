package com.example.demo.service;

import com.example.demo.model.VenueExceptionRules;
import com.example.demo.repository.VenueExceptionRulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VenueExceptionRulesService {

    @Autowired
    private VenueExceptionRulesRepository venueExceptionRulesRepository;

    public List<VenueExceptionRules> getAllExceptionRules() {
        return venueExceptionRulesRepository.findAll();
    }

    public VenueExceptionRules createExceptionRule(VenueExceptionRules exceptionRule) {
        return venueExceptionRulesRepository.save(exceptionRule);
    }

    public VenueExceptionRules getExceptionRuleById(Long id) {
        return venueExceptionRulesRepository.findById(id).orElse(null);
    }

    public void deleteExceptionRule(Long id) {
        venueExceptionRulesRepository.deleteById(id);
    }
}
package com.booking.controller;

import com.booking.controller.TravelPackageController;
import com.booking.entity.TravelPackage;
import com.booking.service.TravelPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
 
@RestController
@RequestMapping("/api/packages")
public class TravelPackageController {
    @Autowired
    private TravelPackageService packageService;
    
    @PostMapping
    public TravelPackage createPackage(@RequestBody TravelPackage travelPackage) {
        return packageService.savePackage(travelPackage);
    }
    
    @PostMapping("/batch")
    public void createPackages(@RequestBody List<TravelPackage> travelPackages) {
    	packageService.savePackages(travelPackages);
    }

 
    @GetMapping("/search-by-destination")
    public List<TravelPackage> searchPackages(@RequestParam String destination) {
        return packageService.searchByDestination(destination);
    }
    
    //Search Packages by country
    @GetMapping("/search-by-country")
    public List<TravelPackage> searchByCountry(@RequestParam String country) {
        return packageService.searchByCountry(country);
    }

    //Search Packages by offer
    @GetMapping("/search-by-offer")
    public List<TravelPackage> searchByOffer(@RequestParam String offer) {
        return packageService.searchByOffer(offer);
    }
 
    //Search Packages by dates
    @GetMapping("/search-by-date")
    public List<TravelPackage> searchByDates(@RequestParam String from, @RequestParam String to) {
        LocalDate startDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        return packageService.searchByDateRange(startDate, endDate);
    }

}

package com.booking.repository;

import com.booking.entity.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
 
public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
    List<TravelPackage> findByDestination(String destination);
    List<TravelPackage> findByOfferContainingIgnoreCase(String offer);
    List<TravelPackage> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate from, LocalDate to);
    List<TravelPackage> findByCountryContainingIgnoreCase(String country);
}


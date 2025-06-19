package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.TravelPackage;

/**
 * Repository for TravelPackage entity.
 */
public interface TravelPackageRepository extends JpaRepository<TravelPackage, Long> {
	List<TravelPackage> findByAgentId(Long agentId);
	List<TravelPackage> findByTitleContainingIgnoreCase(String title);
	List<TravelPackage> findByPriceLessThanEqual(double price);
	List<TravelPackage> findByOffer_CouponCode(String couponCode);

}

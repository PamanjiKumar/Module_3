package com.example.service;

import com.example.client.UserClient;
import com.example.dto.FlightDTO;
import com.example.dto.HotelDTO;
import com.example.dto.ItineraryDTO;
import com.example.dto.OfferDTO;
import com.example.dto.SightseeingDTO;
import com.example.dto.TravelPackageDTO;
import com.example.dto.UserDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.model.Flight;
import com.example.model.Hotel;
import com.example.model.Itinerary;
import com.example.model.Offer;
import com.example.model.Sightseeing;
import com.example.model.TravelPackage;
import com.example.repository.TravelPackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TravelPackageService {

    private static final Logger logger = LoggerFactory.getLogger(TravelPackageService.class);
    private final TravelPackageRepository repository;
    
    @Autowired
    private final UserClient userClient;

    public TravelPackageService(TravelPackageRepository repository,UserClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    // Get all packages
    public List<TravelPackage> getAllPackages() {
        logger.info("Fetching all travel packages...");
        return repository.findAll();
    }

    // Get package by ID
    public TravelPackage getPackageById(Long id) {
        logger.debug("Fetching travel package with ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Package not found with ID: {}", id);
                    return new ResourceNotFoundException("Package not found with ID: " + id);
                });
    }
    //for getting packages using agentid
    public List<TravelPackage> getPackageByAgentId(Long agentId){
    	logger.info("Fetching packages for agent ID: {} ", agentId);
    	
    	List<TravelPackage> packages = repository.findByAgentId(agentId);
    	
    	if(packages.isEmpty()) {
    		logger.warn("No packages found for agent ID: {}", agentId);
    		throw new ResourceNotFoundException("No packages found for agent ID: "+agentId);
    	}
    	
    	return packages;
    }
    
    

    // Create package
    public TravelPackage createPackage(TravelPackage travelPackage) {
    	
    	Long agentId = travelPackage.getAgentId();
    	logger.info("Validating agent ID: {}", agentId);
    	
    	try {
    		UserDTO user = userClient.getUserById(agentId);
    		logger.info("User fetched from User Service: {}", user);
    		
    		if(user == null) {
    			throw new ResourceNotFoundException("User not found for ID: "+agentId);
    		}
    		
    		if(!"AGENT".equalsIgnoreCase(user.getRole())) {
    			logger.warn("User with ID {} is not an agent. Role: {}", agentId, user.getRole());
    			throw new IllegalArgumentException("User is not authorized to create travel packages.");
    			
    		}
    		System.out.println("Agent fetched: "+ user.getName() + "Role" + user.getRole());
    		
    	}catch(Exception e) {
    			logger.error("Feign call failed for agentId {}: {}", agentId, e.getMessage());
    			throw new ResourceNotFoundException("Agent with ID "+ agentId+" not found or invalid.");
    			
    		}
    		return repository.save(travelPackage);
    	}
    	
    	
    	
    	
    	
//        logger.info("Creating new travel package: {}", travelPackage.getTitle());
//        
//       try {
//       	userClient.getUserById(travelPackage.getAgentId());
//       }catch (Exception e) {
//       	logger.error("Agent ID {} not found in User Management Service", travelPackage.getAgentId());
//       	throw new IllegalArgumentException("Agent with ID"+ travelPackage.getAgentId() + " not found");
//        }
//        return repository.save(travelPackage);
    

    // Update package
    public TravelPackage updatePackage(Long id, TravelPackage updatedPackage) {
        TravelPackage existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + id));
     
        existing.setTitle(updatedPackage.getTitle());
        existing.setDescription(updatedPackage.getDescription());
        existing.setDuration(updatedPackage.getDuration());
        existing.setPrice(updatedPackage.getPrice());
        existing.setMaxCapacity(updatedPackage.getMaxCapacity());
        existing.setTripStartDate(updatedPackage.getTripStartDate());
        existing.setTripEndDate(updatedPackage.getTripEndDate());
        existing.setHighlights(updatedPackage.getHighlights());
        existing.setOffer(updatedPackage.getOffer());
     
        // ✅ Safely update child lists — DO NOT use setFlights()
        if (updatedPackage.getFlights() != null) {
            existing.getFlights().clear();
            existing.getFlights().addAll(updatedPackage.getFlights());
        }
     
        if (updatedPackage.getHotels() != null) {
            existing.getHotels().clear();
            existing.getHotels().addAll(updatedPackage.getHotels());
        }
     
        if (updatedPackage.getSightseeing() != null) {
            existing.getSightseeing().clear();
            existing.getSightseeing().addAll(updatedPackage.getSightseeing());
        }
     
        if (updatedPackage.getItinerary() != null) {
            existing.getItinerary().clear();
            existing.getItinerary().addAll(updatedPackage.getItinerary());
        }
     
    return repository.save(existing);
    }
   


    // Delete package
    public void deletePackage(Long id) {
        logger.info("Attempting to delete travel package with ID: {}", id);
        if (!repository.existsById(id)) {
            logger.error("Package not found with ID: {}", id);
            throw new ResourceNotFoundException("Package not found with ID: " + id);
        }
        repository.deleteById(id);
        logger.info("Package deleted successfully with ID: {}", id);
    }
    
    
    //DTO 
    public TravelPackageDTO convertToDTO(TravelPackage pkg) {
        TravelPackageDTO dto = new TravelPackageDTO();
     
        // Basic Fields
        dto.setPackageId(pkg.getPackageId());
        dto.setAgentId(pkg.getAgentId());
        dto.setTitle(pkg.getTitle());
        dto.setDescription(pkg.getDescription());
        dto.setDuration(pkg.getDuration());
        dto.setPrice(pkg.getPrice());
        dto.setMaxCapacity(pkg.getMaxCapacity());
        dto.setTripStartDate(pkg.getTripStartDate());
        dto.setTripEndDate(pkg.getTripEndDate());
        dto.setHighlights(pkg.getHighlights());
     
        // Flights
        List<FlightDTO> flightDTOs = new ArrayList<>();
        for (Flight f : pkg.getFlights()) {
            FlightDTO fd = new FlightDTO();
            fd.setAirline(f.getAirline());
            fd.setFromCity(f.getFromCity());
            fd.setToCity(f.getToCity());
            fd.setDepartureTime(f.getDepartureTime());
            fd.setArrivalTime(f.getArrivalTime());
            flightDTOs.add(fd);
        }
        dto.setFlights(flightDTOs);
     
        // Hotels
        List<HotelDTO> hotelDTOs = new ArrayList<>();
        for (Hotel h : pkg.getHotels()) {
            HotelDTO hd = new HotelDTO();
            hd.setName(h.getName());
            hd.setCity(h.getCity());
            hd.setRating(h.getRating());
            hd.setNights(h.getNights());
            hd.setCostPerNight(h.getCostPerNight());
            hotelDTOs.add(hd);
        }
        dto.setHotels(hotelDTOs);
     
        // Sightseeing
        List<SightseeingDTO> sightseeingDTOs = new ArrayList<>();
        for (Sightseeing s : pkg.getSightseeing()) {
            SightseeingDTO sd = new SightseeingDTO();
            sd.setLocation(s.getLocation());
            sd.setDescription(s.getDescription());
            sightseeingDTOs.add(sd);
        }
        dto.setSightseeing(sightseeingDTOs);
     
        // Itinerary
        List<ItineraryDTO> itineraryDTOs = new ArrayList<>();
        for (Itinerary i : pkg.getItinerary()) {
            ItineraryDTO id = new ItineraryDTO();
            id.setDayNumber(i.getDayNumber());
            id.setActivityTitle(i.getActivityTitle());
            id.setActivityDescription(i.getActivityDescription());
            itineraryDTOs.add(id);
        }
        dto.setItinerary(itineraryDTOs);
     
        // Offer
        Offer offer = pkg.getOffer();
        if (offer != null) {
            OfferDTO od = new OfferDTO();
            od.setCouponCode(offer.getCouponCode());
            od.setDescription(offer.getDescription());
            od.setDiscountPercentage(offer.getDiscountPercentage());
            od.setActive(offer.isActive());
            dto.setOffer(od);
        }
        
        dto.setCountry(pkg.getCountry());
        dto.setDestination(pkg.getDestination());
        dto.setTripType(pkg.getTripType());
     
        return dto;
    }
    
    
    

    

     
}

 
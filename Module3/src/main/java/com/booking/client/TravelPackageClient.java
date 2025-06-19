package com.booking.client;

import com.booking.DTO.TravelPackageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "travel-package-management")
public interface TravelPackageClient {

    @GetMapping("/api/packages/internal/all")
    List<TravelPackageDTO> getAllPackages();

    @GetMapping("/api/packages/{id}")
    TravelPackageDTO getPackageById(@PathVariable("id") Long id);
}
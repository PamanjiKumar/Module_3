package com.example.usermanagementservice.feign;

import com.example.usermanagementservice.dto.TravelPackageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "travel-package-management")
public interface TravelPackageClient {
    @GetMapping("/api/packages/admin/agent/{agentId}")
    List<TravelPackageDTO> getPackagesByAgent(@PathVariable("agentId") Long id);
}
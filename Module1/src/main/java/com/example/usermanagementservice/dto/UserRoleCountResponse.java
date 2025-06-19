package com.example.usermanagementservice.dto;

public class UserRoleCountResponse {
    private long totalUsers;
    private long agentCount;
    private long customerCount;
 
    public UserRoleCountResponse(long totalUsers, long agentCount, long customerCount) {
        this.totalUsers = totalUsers;
        this.agentCount = agentCount;
        this.customerCount = customerCount;
    }
 
    // Getters
    public long getTotalUsers() { return totalUsers; }
    public long getAgentCount() { return agentCount; }
    public long getCustomerCount() { return customerCount; }
}

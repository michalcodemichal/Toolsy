package com.toolsy.dto.response;

import java.math.BigDecimal;

public class StatisticsResponse {
    private Long totalTools;
    private Long totalUsers;
    private Long totalRentals;
    private Long activeRentals;
    private BigDecimal totalRevenue;
    private Long availableTools;

    public StatisticsResponse() {
    }

    public Long getTotalTools() {
        return totalTools;
    }

    public void setTotalTools(Long totalTools) {
        this.totalTools = totalTools;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalRentals() {
        return totalRentals;
    }

    public void setTotalRentals(Long totalRentals) {
        this.totalRentals = totalRentals;
    }

    public Long getActiveRentals() {
        return activeRentals;
    }

    public void setActiveRentals(Long activeRentals) {
        this.activeRentals = activeRentals;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getAvailableTools() {
        return availableTools;
    }

    public void setAvailableTools(Long availableTools) {
        this.availableTools = availableTools;
    }
}


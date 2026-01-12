package com.toolsy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Odpowiedź zawierająca statystyki systemu")
public class StatisticsResponse {
    @Schema(description = "Całkowita liczba narzędzi", example = "30")
    private Long totalTools;
    @Schema(description = "Całkowita liczba użytkowników", example = "50")
    private Long totalUsers;
    @Schema(description = "Całkowita liczba wypożyczeń", example = "150")
    private Long totalRentals;
    @Schema(description = "Liczba aktywnych wypożyczeń", example = "10")
    private Long activeRentals;
    @Schema(description = "Całkowity przychód", example = "5000.00")
    private BigDecimal totalRevenue;
    @Schema(description = "Liczba dostępnych narzędzi", example = "25")
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








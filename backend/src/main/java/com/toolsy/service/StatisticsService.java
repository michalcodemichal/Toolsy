package com.toolsy.service;

import com.toolsy.dto.response.StatisticsResponse;
import com.toolsy.model.RentalStatus;
import com.toolsy.model.ToolStatus;
import com.toolsy.repository.RentalRepository;
import com.toolsy.repository.ToolRepository;
import com.toolsy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StatisticsService {
    private final ToolRepository toolRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public StatisticsService(ToolRepository toolRepository, UserRepository userRepository,
                            RentalRepository rentalRepository) {
        this.toolRepository = toolRepository;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    public StatisticsResponse getStatistics() {
        StatisticsResponse stats = new StatisticsResponse();
        
        stats.setTotalTools((long) toolRepository.count());
        stats.setTotalUsers((long) userRepository.count());
        stats.setTotalRentals((long) rentalRepository.count());
        stats.setActiveRentals((long) rentalRepository.findByStatus(RentalStatus.ACTIVE).size());
        stats.setAvailableTools((long) toolRepository.findByStatus(ToolStatus.AVAILABLE).size());
        
        BigDecimal totalRevenue = rentalRepository.findAll().stream()
                .filter(r -> r.getTotalPrice() != null && 
                        (r.getStatus() == RentalStatus.COMPLETED || r.getStatus() == RentalStatus.ACTIVE))
                .map(r -> r.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.setTotalRevenue(totalRevenue);
        
        return stats;
    }
}




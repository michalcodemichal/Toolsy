package com.toolsy.repository;

import com.toolsy.model.Rental;
import com.toolsy.model.RentalStatus;
import com.toolsy.model.User;
import com.toolsy.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser(User user);
    List<Rental> findByTool(Tool tool);
    List<Rental> findByStatus(RentalStatus status);
    List<Rental> findByUserAndStatus(User user, RentalStatus status);
    
    @Query("SELECT r FROM Rental r WHERE r.tool = :tool AND r.status IN :statuses AND " +
           "((r.startDate <= :endDate AND r.endDate >= :startDate))")
    List<Rental> findOverlappingRentals(@Param("tool") Tool tool, 
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        @Param("statuses") List<RentalStatus> statuses);
    
    @Query("SELECT r FROM Rental r WHERE r.endDate < :currentDate AND r.status = :status")
    List<Rental> findOverdueRentals(@Param("currentDate") LocalDate currentDate, 
                                     @Param("status") RentalStatus status);
}


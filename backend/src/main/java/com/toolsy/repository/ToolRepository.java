package com.toolsy.repository;

import com.toolsy.model.Tool;
import com.toolsy.model.ToolStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findByCategory(String category);
    List<Tool> findByStatus(ToolStatus status);
    List<Tool> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT t FROM Tool t WHERE t.status = :status AND t.quantity > 0")
    List<Tool> findAvailableTools(@Param("status") ToolStatus status);
    
    @Query("SELECT t FROM Tool t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Tool> searchTools(@Param("searchTerm") String searchTerm);
}


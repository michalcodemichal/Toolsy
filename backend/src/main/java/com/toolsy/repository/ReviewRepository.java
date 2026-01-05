package com.toolsy.repository;

import com.toolsy.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByToolIdOrderByCreatedAtDesc(Long toolId);
    
    Optional<Review> findByUserIdAndToolId(Long userId, Long toolId);
    
    boolean existsByUserIdAndToolId(Long userId, Long toolId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tool.id = :toolId")
    Double findAverageRatingByToolId(@Param("toolId") Long toolId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.tool.id = :toolId")
    Long countByToolId(@Param("toolId") Long toolId);
}


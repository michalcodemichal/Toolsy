package com.toolsy.repository;

import com.toolsy.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByToolIdOrderByCreatedAtDesc(Long toolId);

    boolean existsByToolIdAndUserId(Long toolId, Long userId);

    @Query("select avg(r.rating) from Review r where r.tool.id = :toolId")
    Double findAverageRatingByToolId(@Param("toolId") Long toolId);

    long countByToolId(Long toolId);
}

package com.toolsy.repository;

import com.toolsy.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByToolId(Long toolId);
    Optional<Review> findByToolIdAndUserId(Long toolId, Long userId);
    boolean existsByUserIdAndToolId(Long userId, Long toolId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tool.id = :toolId")
    Double findAverageRatingByToolId(Long toolId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.tool.id = :toolId")
    Long countByToolId(Long toolId);
}


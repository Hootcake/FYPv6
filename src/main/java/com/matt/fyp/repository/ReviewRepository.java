package com.matt.fyp.repository;

import com.matt.fyp.domain.Review;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Review entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select review from Review review where review.reviewed_by.login = ?#{principal.username}")
    List<Review> findByReviewed_byIsCurrentUser();

}

package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query("SELECT COALESCE(ROUND(AVG(r.rate), 1), 0.0) " +
            "FROM Rating r " +
            "WHERE r.ratingId.post.id = :postId " +
            "AND (r.ratingId.post.publicationDate IS NOT NULL " +
            "AND r.ratingId.post.publicationDate < :now)")
    float getAverage(int postId, LocalDateTime now);
}

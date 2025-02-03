package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Rating extends CreationUpdate {
    @EmbeddedId
    private RatingId ratingId;
    private byte rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return Objects.equals(ratingId, rating.ratingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratingId);
    }

    /*
    * Rating rating = new Rating(new RatingId(user, post), rate);
    *     * */
}

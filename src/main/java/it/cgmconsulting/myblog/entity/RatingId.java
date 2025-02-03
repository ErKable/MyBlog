package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable //si usa quando la PK è composita  oppure quanto è una FK
//anche PK singola ma FK allo stesso tempo
//lo usiamo quando la pk di una tabella è composita oppure e PK e FK allo stesso tempo
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RatingId implements Serializable { //le classi embeddable devono implementare Serializable perché altrimenti va in conflitto con le librerie jackson
    //questa è la classe che rappresenta la pk della classe rating
    //questa è necessaria perchè abbiamo una pk doppia
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return Objects.equals(user, ratingId.user) && Objects.equals(post, ratingId.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, post);
    }
}

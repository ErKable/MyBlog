package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.Creation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Creation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //per primarykey autoincrementale
    private int id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @Column(nullable = false)
    private String comment;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;
    private boolean censored = false;
    @ManyToOne
    private Comment parent;

    public Comment(User user, String comment, Post post, Comment parent) {
        this.user = user;
        this.comment = comment;
        this.post = post;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

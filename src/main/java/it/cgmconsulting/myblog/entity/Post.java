package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
public class Post extends CreationUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //per primarykey autoincrementale
    private int id;
    @ManyToOne
    @JoinColumn(nullable = false) //quando fanno parte di una relazione
    private User user;
    @Column(nullable = false, length = 100)
    private String postTitle;
    private LocalDateTime publicationDate;
    @ManyToMany//(fetch = FetchType.EAGER) // tutte le many to many hanno il fetch time a lazy perche essendo una molti a molti
    // non sappiamo a priori quanti sono questi molti
    //con fetch type eager si prende tutti i dati anche quelli
    // che non dovrebbero apparire tipo la pw del writer e altro
    @JoinTable(name="post_tags", //join table crea la tabella di relazione tra le relazioni N a N
        joinColumns = {@JoinColumn(name="post_id", referencedColumnName = "id")}, //referenzia la colonna dell'entita in cui ci troviam
        inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")} //si riferisce all'id dell'altra tabella
    )
    private Set<Tag> tags = new HashSet<>();

    public Post(User user, String postTitle) {
        this.user = user;
        this.postTitle = postTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

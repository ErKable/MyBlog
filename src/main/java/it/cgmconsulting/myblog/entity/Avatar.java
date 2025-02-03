package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;
//Negli import delle entità ci devono essere solo questi import
//e devono servire SOLO a mappare le entità presenti nel DB
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //per primarykey autoincrementale
    private int id;
    @Column(nullable = false)
    private String filename;
    @Column(nullable = false, length = 10)
    private String filetype;
    @Lob //gli indichiamo che è un file e lui crea un blob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] data;

    public Avatar(String filename, String filetype, byte[] data) {
        this.filename = filename;
        this.filetype = filetype;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return id == avatar.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

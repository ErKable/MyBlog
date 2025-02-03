package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity //comunichiamo all'app che questa classe ha una corrispondneza
//sul DB e crea all'avvio crea una tabella che segue la struttura della classe
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Tag {
    @Id //con questa notation diciamo che questo attributo sara la PK della tabella
    @Column(length = 50) //definiamo la dimensione massima, questa notation ha diversi parametri
    private String id;
    private boolean visible = true;

    public Tag(String id){
        this.id = id.toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

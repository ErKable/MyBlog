package it.cgmconsulting.myblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SectionImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //per primarykey autoincrementale
    private int id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Section section;
    @Column(nullable = false, length = 4)
    private String filetype;
    private String altTag;

    public SectionImages(Section section, String filetype, String altTag) {
        this.section = section;
        this.filetype = filetype;
        this.altTag = altTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SectionImages that = (SectionImages) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.enumerated.SectionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //per primarykey autoincrementale
    private int id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Post post;
    @Column(length = 100)
    private String sectionTitle;
    @Column(length = 10000, nullable = false)
    private String sectionContent;
    private byte sectionOrder;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SectionType sectionType;

    public Section(Post post, String sectionTitle, String sectionContent, byte sectionOrder, SectionType sectionType) {
        this.post = post;
        this.sectionTitle = sectionTitle;
        this.sectionContent = sectionContent;
        this.sectionOrder = sectionOrder;
        this.sectionType = sectionType;
    }

    public Section(Post post, String sectionContent, byte sectionOrder, SectionType sectionType) {
        this.post = post;
        this.sectionContent = sectionContent;
        this.sectionOrder = sectionOrder;
        this.sectionType = sectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id == section.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.dto.response.SectionResponse;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Section;
import it.cgmconsulting.myblog.entity.enumerated.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface SectionRepository extends JpaRepository<Section, Integer> {

    Set<Section> findAllByPostId(int postId);

    boolean existsBySectionTypeAndPost(SectionType sectionType, Post post);

    @Query("SELECT new it.cgmconsulting.myblog.dto.response.SectionResponse(" +
            "s.id, " +
            "s.sectionTitle, " +
            "s.sectionContent, " +
            "s.sectionOrder, " +
            "s.sectionType " +
            ") FROM Section s " +
            "WHERE s.post.id = :postId")
    List<SectionResponse> getSectionByPostId(int postId);

    @Query(value = "SELECT s.sectionContent FROM Section s WHERE s.post.id = :postId")
    List<String> getSectionsSectionContentByPostId(int postId);
}

package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.SectionImages;
import it.cgmconsulting.myblog.dto.response.SectionImagesResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionImagesRepository extends JpaRepository<SectionImages, Integer> {

    //in JPQL con || contcateniamo
    @Query(value = "SELECT si.id || '.' || si.filetype FROM SectionImages si WHERE si.section.id = :sectionId")
    List<String> filesToDelete(int sectionId);

    @Query("SELECT new it.cgmconsulting.myblog.dto.response.SectionImagesResponse(" +
            ":path || si.id || '.' || si.filetype, " +
            "si.altTag) " +
            "FROM SectionImages si WHERE si.section.id = :sectionId")
    List<SectionImagesResponse> getSectionImages(int sectionId, String path);

    @Modifying
    @Transactional
    //tutte le query di update and delete hanno bisogno di queste due
    //notation altrimenti non funzionano, valido per quelle scritte in JPQL e SQL nativo
    @Query(value = "DELETE FROM section_images si WHERE si.section_id = :sectionId",
            nativeQuery = true)
    void deleteSectionImagesBySectionId(int sectionId);
}

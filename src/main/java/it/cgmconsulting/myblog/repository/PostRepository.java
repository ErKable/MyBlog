package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.dto.response.PostBoxResponse;
import it.cgmconsulting.myblog.dto.response.PostKeywordResponse;
import it.cgmconsulting.myblog.dto.response.PostResponse;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.enumerated.SectionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByPublicationDateIsNotNullAndPublicationDateBefore(LocalDateTime now);

    Optional<Post> findByIdAndPublicationDateIsNotNullAndPublicationDateBefore(int id, LocalDateTime now);
    @Query(value="SELECT new it.cgmconsulting.myblog.dto.response.PostResponse(" +
            "p.id, " +
            "p.postTitle, " +
            "p.publicationDate, " +
            "p.user.username" +
            ") FROM Post p " +
            "WHERE p.publicationDate IS NOT NULL AND p.publicationDate < :now ")
    Page<PostResponse> getPaginatedPosts(Pageable pageable, @Param("now") LocalDateTime now);


    @Query(value="SELECT new it.cgmconsulting.myblog.dto.response.PostKeywordResponse(" +
            "p.id, " +
            "p.postTitle," +
            "p.publicationDate," +
            "p.user.username " +
            ") FROM Post p " +
            "WHERE p.publicationDate IS NOT NULL AND p.publicationDate < :now " +
            "AND p.postTitle LIKE :keyword " +
            "ORDER BY p.publicationDate DESC")
    Page<PostKeywordResponse> getPaginatedPublishedPosts(@Param("now") LocalDateTime now, Pageable pageable, String keyword);

    @Query(value="SELECT new it.cgmconsulting.myblog.dto.response.PostBoxResponse(" +
            "si.section.post.id as postId, " +
            "si.section.post.postTitle as postTitle, " +
            "si.section.sectionContent as sectionContent," +
            ":path || si.id || '.' || si.filetype as image, " +
                "(SELECT COALESCE(ROUND(AVG(r.rate),1), 0.0) FROM Rating r WHERE r.ratingId.post.id = si.section.post.id) as average, " +
                "si.section.post.publicationDate as publicationDate) " +
            " FROM SectionImages si " +
            "WHERE si.section.sectionType = :sectionType " +
            "AND (si.section.post.publicationDate IS NOT NULL AND si.section.post.publicationDate < :now) ")
    Page<PostBoxResponse> getBoxes(Pageable pageable, @Param("now") LocalDateTime now, SectionType sectionType, String path);


    @Query(value= "SELECT new it.cgmconsulting.myblog.dto.response.PostResponse(" +
            "p.id, " +
            "p.postTitle," +
            "p.publicationDate," +
            "p.user.username " +
            ") FROM Post p " +
            "INNER JOIN p.tags tag " +
            "WHERE tag.id= :tag " +
            "AND (p.publicationDate IS NOT NULL AND p.publicationDate < :now) ")
    Page<PostResponse> getPostsByTag(Pageable pageable, LocalDateTime now, String tag);

    /*
    * SQL:
    * SELECT p.id, p.post_title, p.publication_date, u.username
    * FROM post p, user_ u
    * WHERE u.id = p.user_id
    * AND p.publication_date IS NOT NULL AND p.publication_date < CURRENT_TIMESTAMP
    * */
    //JPQL è un mix di Java e SQL quindi usiamo le sintassi di entrambi
    @Query(value="SELECT new it.cgmconsulting.myblog.dto.response.PostResponse(" +
            "p.id, " +
            "p.postTitle, " +
            "p.publicationDate, " +
            "p.user.username " +
             //il punto fa tipo da get e non da concatenatore in questo caso
            ") FROM Post p " +
            "WHERE p.publicationDate IS NOT NULL AND p.publicationDate < :now " +
            "ORDER BY p.publicationDate DESC")//:nomeParametro per accedere ai parametri
    List<PostResponse> getPublishedPosts(LocalDateTime now);

    //altro modo per recuperare il tags associati ad un post
    //che torna subito un array di stringhe senza parsarle
    // come ho fatto io
    @Query(value="SELECT tag.id " +
            "FROM Post p " +
            "INNER JOIN p.tags tag " + //facciamo il join sul set di tag che sul db
            //è la tabella di relazione
            "WHERE p.id = :postId AND tag.visible = true")
    String[] getTagsByPost(@Param("postId") long postId);


    @Query(value="SELECT new it.cgmconsulting.myblog.dto.response.PostResponse(" +
            "p.id, " +
            "p.postTitle, " +
            "p.publicationDate, " +
            "p.user.username " +
            //il punto fa tipo da get e non da concatenatore in questo caso
            ") FROM Post p " +
            "WHERE p.id = :postId AND " +
            "p.publicationDate IS NOT NULL AND p.publicationDate < :now " +
            "ORDER BY p.publicationDate DESC")//:nomeParametro per accedere ai parametri
    PostResponse getPublishedPost(LocalDateTime now, int postId);


    /* SI PUÒ CAMBIARE ANCHE IL NOME DEL PARAMETRO PASSATO AL METODO
    * @Query("SELECT new it.cgmconsulting.myblog.dto.response.PostResponse(" +
            "p.id," +
            "p.postTitle," +
            "p.publicationDate" +
            "p.user.username"+ //il punto fa tipo da get e non da concatenatore in questo caso
            ")FROM Post as P" +
            "WHERE p.publicationDate IS NOT NULL AND p.publicationDate < :currentDate")
    * List<PostResponse> getPublishedPosts(@Param("currentDate")LocalDateTime now);
    * */
}

package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.dto.response.CommentResponse;
import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT new it.cgmconsulting.myblog.dto.response.CommentResponse(" +
            "c.id, " +
            //"c.comment, " +
            "CASE WHEN (c.censored = true) THEN '***********' " +
            "ELSE c.comment END, " +
            "c.user.username, " +
            "c.createdAt, " +
            "c.parent.id) " +
            /*
            * se volessi lasciare il primitivo
            * COALESCE(c.parent.id), 0),
            * */
            "FROM Comment c " +
            "WHERE c.post.id = :postId " +
            //"AND c.censored = false " +
            "AND (c.post.publicationDate IS NOT NULL " +
            "AND c.post.publicationDate < :now) " +
            "ORDER BY c.createdAt")
    List<CommentResponse> getCommentsByPostId(int postId, LocalDateTime now);

    //Optional<User> findUserByCommentId(int commentId);

}

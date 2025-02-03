package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.dto.request.CommentRequest;
import it.cgmconsulting.myblog.dto.response.CommentResponse;
import it.cgmconsulting.myblog.entity.Comment;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    public boolean addComment(UserDetails userDetails, CommentRequest request) {
        Post post = postService.findPublishedPostById(request.postId(), LocalDateTime.now());
        Comment parent = request.parentId() != null ? findById(request.parentId()) : null;
        Comment comment = new Comment((User) userDetails, request.comment(), post, parent);
        commentRepository.save(comment);
        return true;
    }

    protected Comment findById(int id){
        return commentRepository.findById(id).orElseThrow();
    }

    public List<CommentResponse> getCommentsByPostId(int postId){
        return commentRepository.getCommentsByPostId(postId, LocalDateTime.now());
    }

/*    public User getUserByCommentInd(int commentId){
        return commentRepository.findUserByCommentId(commentId).orElseThrow();
    }*/
}

package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.dto.request.CommentRequest;
import it.cgmconsulting.myblog.dto.response.CommentResponse;
import it.cgmconsulting.myblog.service.CommentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> addComment(@AuthenticationPrincipal UserDetails userDetails,
                                        @Valid @RequestBody CommentRequest request){

        return ResponseHandler.generateResponse(HttpStatus.CREATED, commentService.addComment(userDetails, request));
    }

    @GetMapping("/p")
    public ResponseEntity<?> getComments(@RequestParam @Min(1) int postId){
        //List<CommentResponse>
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        if(comments.isEmpty())
            return ResponseHandler.generateResponse(HttpStatus.NOT_FOUND, "No comments found for post "+postId);
        else
            return ResponseHandler.generateResponse(HttpStatus.OK, comments);
    }
}

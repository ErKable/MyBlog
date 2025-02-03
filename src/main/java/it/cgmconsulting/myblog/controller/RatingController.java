package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.service.RatingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
@Validated
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/{postId}/{rate}")
    @PreAuthorize("hasAuthority('MEMBER') or hasAuthority('WRITER')")
    public ResponseEntity<?>  ratePost(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable @Min(1) int postId,
                                       @PathVariable @Min(1) @Max(5) byte rate){
/*        if(ratingService.ratePost(userDetails, postId, rate))
            return ResponseHandler.generateResponse(HttpStatus.OK, "Post voted");
        else
            return ResponseHandler.generateResponse(HttpStatus.CONFLICT, "You already voted this post");*/
        return ResponseHandler.generateResponse(HttpStatus.OK, ratingService.ratePost(userDetails, postId, rate));
    }

    @GetMapping("/p/{postId}")
    public ResponseEntity<?> average(@PathVariable @Min(1) int postId){
        return ResponseHandler.generateResponse(HttpStatus.OK, ratingService.getAverage(postId));
    }
}

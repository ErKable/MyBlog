package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Rating;
import it.cgmconsulting.myblog.entity.RatingId;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.repository.PostRepository;
import it.cgmconsulting.myblog.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final PostService postService;

    public byte ratePost(UserDetails userDetails, int postId, byte rate){
        Post post = postService.findPublishedPostById(postId, LocalDateTime.now());
        RatingId ratingId = new RatingId((User) userDetails, post);
        /*if(!ratingRepository.findById(ratingId).isEmpty())
            return false;*/
        Rating rating = new Rating(ratingId, rate);
        ratingRepository.save(rating);
        return rate;
    }

    public float getAverage(int postId){
/*        Optional<Post> post = postService.findPublishedPostByIdOpt(postId, LocalDateTime.now());
        if(post.isEmpty())
            return 0.0f;*/
        return ratingRepository.getAverage(postId, LocalDateTime.now());
    }
}

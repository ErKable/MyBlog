package it.cgmconsulting.myblog.dto.response;

import it.cgmconsulting.myblog.entity.Tag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostResponse {
    private int postId;
    private String postTitle;
    private LocalDateTime publicationDate;
    private String username;
    private String[] tags;

    public PostResponse(int postId, String postTitle, LocalDateTime publicationDate, String username) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.publicationDate = publicationDate;
        this.username = username;
    }
}

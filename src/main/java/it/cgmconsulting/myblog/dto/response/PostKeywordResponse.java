package it.cgmconsulting.myblog.dto.response;

import it.cgmconsulting.myblog.entity.Section;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostKeywordResponse {
    //poi estrapolo solo la PostResponse da mandare al FRONT END
    private int postId;
    private String postTitle;
    private LocalDateTime publicationDate;
    private String username;
    private List<String> sectionsContent;

    public PostKeywordResponse(int postId, String postTitle, LocalDateTime publicationDate, String username) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.publicationDate = publicationDate;
        this.username = username;
    }

    public PostResponse getPostResponse() {
        return new PostResponse(postId, postTitle, publicationDate, username);
    }
}

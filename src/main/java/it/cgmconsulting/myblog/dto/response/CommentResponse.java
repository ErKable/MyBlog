package it.cgmconsulting.myblog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class CommentResponse {
    private int commentId;
    private String comment;
    private String username;
    private LocalDateTime createdAt;
    private Integer parentId;
}

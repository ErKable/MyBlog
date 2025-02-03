package it.cgmconsulting.myblog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostBoxResponse {

    private int postId;
    private String postTitle;

    // Dati tratti da Section avente sectionType 'HE'
    private String sectionContent;
    private String image; // path+nomefile+estensione

    private double average; // voto medio

    private LocalDateTime publicationDate;

    private String[] tags;

    public PostBoxResponse(int postId, String postTitle, String sectionContent, String image, double average, LocalDateTime publicationDate) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.sectionContent = sectionContent;
        this.image = image;
        this.average = average;
        this.publicationDate = publicationDate;
    }
}

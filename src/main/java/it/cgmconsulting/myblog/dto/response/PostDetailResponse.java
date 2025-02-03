package it.cgmconsulting.myblog.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PostDetailResponse {
    private PostResponse postResponse;
    private List<SectionResponse> sections;
}

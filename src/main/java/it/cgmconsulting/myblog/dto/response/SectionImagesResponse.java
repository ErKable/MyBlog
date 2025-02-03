package it.cgmconsulting.myblog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter  @NoArgsConstructor @AllArgsConstructor
public class SectionImagesResponse {
    private String filename;
    private String altTag;
/*    @Value("${app.image.section.path}")
    private String path; DA NULL*/


}

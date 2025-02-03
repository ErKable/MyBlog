package it.cgmconsulting.myblog.dto.response;

import it.cgmconsulting.myblog.entity.enumerated.SectionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor

public class SectionResponse {
    private int id;
    private String sectionTitle;
    private String sectionContent;
    private byte sectionOrder;
    private SectionType sectionType;
    List<SectionImagesResponse> images;

    public SectionResponse(int id, String sectionTitle, String sectionContent, byte sectionOrder, SectionType sectionType) {
        this.id = id;
        this.sectionTitle = sectionTitle;
        this.sectionContent = sectionContent;
        this.sectionOrder = sectionOrder;
        this.sectionType = sectionType;
    }
}

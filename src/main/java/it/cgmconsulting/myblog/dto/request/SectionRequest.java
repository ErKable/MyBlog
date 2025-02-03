package it.cgmconsulting.myblog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SectionRequest {
    @NotNull @Min(1)
    private int postId;
    @Size(max = 100)
    private String sectionTitle;
    @NotBlank
    @Size(max = 10000, min = 100)
    private String sectionContent;
    @NotBlank @Size(min = 2, max = 2)
    private String sectionType;
    @NotNull
    private byte sectionOrder;
}

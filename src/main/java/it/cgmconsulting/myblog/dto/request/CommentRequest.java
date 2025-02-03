package it.cgmconsulting.myblog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest (
        @Min(1) int postId,
        @NotBlank @Size(max=255) String comment,
        Integer parentId
        //Integer perché può essere null in qunato non è detto che sia riferito ad un altro commento
){}

package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.dto.request.SectionRequest;
import it.cgmconsulting.myblog.service.ImageService;
import it.cgmconsulting.myblog.service.SectionService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController @RequestMapping("section")
@Validated @RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;
    private final ImageService imageService;
    @Value("${app.image.section.size}")
    private long size;
    @Value("${app.image.section.width}")
    private int width;
    @Value("${app.image.section.height}")
    private int height;
    @Value("${app.image.section.extensions}")
    private String[] extension;
    @Value("${app.image.section.path}")
    private String path;

    @PostMapping
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> addSection(@RequestBody SectionRequest request){
        return ResponseHandler.generateResponse(HttpStatus.CREATED, sectionService.addSection(request));
    }

    @PostMapping("/{sectionId}")
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> updateSection(@RequestBody SectionRequest request, @PathVariable @Min(1) int sectionId){
        return ResponseHandler.generateResponse(HttpStatus.CREATED, sectionService.updateSection(request, sectionId));
    }

    @PutMapping(value = "/{sectionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> addImages(@RequestParam MultipartFile file, @PathVariable @Min(1) int sectionId, @RequestParam(required = false) @Size(max=255) String altTag){
        if(imageService.checkSize(file, size))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "File too large");
        if(imageService.checkDimensions(file, width, height))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "Wrong width or height");
        if(!imageService.checkExtensions(file, extension))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "File type not allowed");
        return ResponseHandler.generateResponse(HttpStatus.OK, sectionService.addImages(file, sectionId, altTag, path));
    }

    @DeleteMapping("/image/{sectionImagesId}")
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> removeImage(@PathVariable @Min(1) int sectionImagesId){
        return ResponseHandler.generateResponse(HttpStatus.OK, sectionService.removeImage(sectionImagesId, path));
    }

    @DeleteMapping("{sectionId}")
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> removeSection(@PathVariable @Min(1) int sectionId){
        //rimuovere eventuali Section images collegate alla sezione

        //eliminare la sezione
        return ResponseHandler.generateResponse(HttpStatus.OK, sectionService.removeSection(sectionId, path));

    }

}

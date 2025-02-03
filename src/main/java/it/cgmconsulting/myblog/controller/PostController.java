package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.service.PostService;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("api/posts/")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> createPost(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestParam @NotBlank @Size(max=100, min = 3) String title){
        return ResponseHandler.generateResponse(HttpStatus.OK, postService.createPost(userDetails, title));
    }

    @GetMapping("/p")
    public ResponseEntity<?> getPosts(){
        return ResponseHandler.generateResponse(HttpStatus.OK, postService.getPosts());
    }


    @GetMapping("/p/pagination")
    public ResponseEntity<?> getPaginationPosts(
            @RequestParam(defaultValue = "0") int pageNumber, // numero pagina da cui partire
            @RequestParam(defaultValue = "2") int pageSize, // numero di lementi per pagina
            @RequestParam(defaultValue = "publicationDate") String sortBy, // colonna su cui applicare l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // ordinamento della colonna sortBy ASC or DESC
    ){
        return ResponseHandler.generateResponse(HttpStatus.OK, postService.getPaginatedPosts(pageNumber, pageSize, direction, sortBy));
    }

    @GetMapping("/p/tag")
    public ResponseEntity<?> getPostsByTag(@RequestParam(defaultValue = "0") int pageNumber, // numero pagina da cui partire
                                           @RequestParam(defaultValue = "2") int pageSize, // numero di lementi per pagina
                                           @RequestParam(defaultValue = "publicationDate") String sortBy, // colonna su cui applicare l'ordinamento
                                           @RequestParam(defaultValue = "DESC") String direction,
                                           @RequestParam @NotBlank @Size(max = 50, min = 3) String tag){
        return ResponseHandler.generateResponse(HttpStatus.OK, postService.getPostsByTag(pageNumber, pageSize, direction, sortBy, tag));
    }



    @GetMapping("/p/boxes")
    public ResponseEntity<?> getBoxes(@RequestParam(defaultValue = "0") int pageNumber, // numero pagina da cui partire
                                      @RequestParam(defaultValue = "2") int pageSize, // numero di lementi per pagina
                                      @RequestParam(defaultValue = "publicationDate") String sortBy, // colonna su cui applicare l'ordinamento
                                      @RequestParam(defaultValue = "DESC") String direction){
        return ResponseHandler.generateResponse(HttpStatus.OK, postService.getBoxes(pageNumber, pageSize, direction, sortBy));
    }

    @PutMapping("/{postId}/tags")
    @PreAuthorize("hasAuthority('WRITER')")
    public ResponseEntity<?> addTags(@RequestParam @NotEmpty Set<String> tags,
                                     @PathVariable @Min(1) int postId){
        postService.addTags(tags, postId);
        return ResponseHandler.generateResponse(HttpStatus.OK, "Tags succesfully joined to post");
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    //la data va passata cosi: 2024-01-17T13:11:40
    public ResponseEntity<?> publishPost(@RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime publicationDate, @RequestParam @NotNull @Min(1) int postId){
        if(publicationDate.isBefore(LocalDateTime.now()))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "Set a valid publication date");
        postService.setPublicationDate(publicationDate, postId);
        return ResponseHandler.generateResponse(HttpStatus.OK, "Publication date has been set");
    }

    @GetMapping("/p/detail/{postId}")
    public ResponseEntity<?> getPostDetails(@PathVariable @Min(1) int postId){
        return ResponseHandler.generateResponse(HttpStatus.OK, postService.getPostDetail(postId));
    }

    @GetMapping("/p/search") //ricerca i post nelle cui sectionContent o nel postTitle compare la keyword.
    public ResponseEntity<?> getPostsByKeyword(@RequestParam(defaultValue = "0") int pageNumber, //pagina da cui partire
                                               @RequestParam(defaultValue = "2") int pageSize,
                                               @RequestParam(defaultValue = "publicationDate") String sortBy, //Lo vado a cercare nel risuultato della query in base al nome delle tabelle
                                               @RequestParam(defaultValue = "DESC") String direction,
                                               @RequestParam @NotBlank @Size(max = 50) String keyword,
                                               @RequestParam boolean isCaseSensitive,
                                               @RequestParam boolean isExactMatch) {
        return ResponseHandler.generateResponse(HttpStatus.OK, postService.getPostsByKeyword(pageNumber, pageSize, direction, sortBy, keyword, isCaseSensitive, isExactMatch));
    }
}

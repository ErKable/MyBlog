package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.service.TagService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("tags") //tutti gli end point di questa classe rispondono all'URI tags
//eventualmente seguito da ulteriori parti definite nei singoli mapping
@Validated // per far fare i controlli che definiamo nei parametri (vedi add tag)
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService){
        this.tagService = tagService;
    }
    //invece di tornare un set ritorniamo un ResponseEntity
    //Cosi facendo possiamo decidere noi lo stato della risposta
    //grazie a questo possiamo "modificare" il comportamento dell'app
    // perche se fa la chiamata a prescindere dalla risposta
    // lui dara OK, il response entity ci consente di modificare lo stato
    @GetMapping("/p/visible")
    public ResponseEntity<?> getAllVisibleTags(){
        Set<String> tags = tagService.getVisibleTags();
        if(tags.isEmpty())
            return ResponseHandler.generateErrorResponse(HttpStatus.NOT_FOUND, "No tags found");
        else
            return ResponseHandler.generateResponse(HttpStatus.OK, tags);
    }
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')") // notazione parametrizzata con il ruolo di chi può invocare questo endpoint
    public ResponseEntity<?> getAllTags(){
        List<Tag> tags = tagService.findAll();
        if(tags.isEmpty())
            return new ResponseEntity<>("No tags found" , HttpStatus.NOT_FOUND);
        else
            return ResponseHandler.generateResponse(HttpStatus.OK, tags);
    }

/*    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addTag(@RequestParam @NotEmpty String tag){
        //verificare che il tag non esistà già
        //salvare il tag
        tag = tag.toUpperCase();
        if(!tagService.save(tag)) return ResponseHandler.generateResponse(HttpStatus.CREATED, tag);
        else return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT, "Tag '"+tag+"' already present");
    }*/
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addTag2(@RequestParam @NotEmpty Set<String> tags){
        if(tagService.tagsToAdd(tags))
            return ResponseHandler.generateResponse(HttpStatus.CREATED, "New tags added");
        else
            return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT, "No tags added");
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> switchVisibility(@RequestBody @NotEmpty Set<Tag> tags){
        tagService.switchVisibility(tags);
        return ResponseHandler.generateResponse(HttpStatus.OK, "Tags visibility modified");
    }



/*    @GetMapping("/visible-sql")
    public ResponseEntity<?> getAllVisibleTagsSQL(){
        Set<String> tags = tagService.getVisibleTags();
        if(tags.isEmpty())
            return new ResponseEntity<>("No tags found", HttpStatus.NOT_FOUND);
        else
            return ResponseHandler.generateResponse(HttpStatus.OK, tags);;
    }

    @GetMapping("/visible-jpql")
    public ResponseEntity<?> getAllVisibleTagsJPQL(){
        Set<String> tags = tagService.getVisibleTagsJPQL();
        if(tags.isEmpty())
            return new ResponseEntity<>("No tags found", HttpStatus.NOT_FOUND);
        else
            return ResponseHandler.generateResponse(HttpStatus.OK, tags);
    }*/
}

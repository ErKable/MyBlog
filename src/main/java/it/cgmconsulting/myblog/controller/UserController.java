package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.dto.request.ChangePwdRequest;
import it.cgmconsulting.myblog.dto.request.ModifyMeRequest;
import it.cgmconsulting.myblog.entity.enumerated.Role;
import it.cgmconsulting.myblog.service.ImageService;
import it.cgmconsulting.myblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ImageService imageService;
    @Value("${app.image.avatar.size}")
    private long size;
    @Value("${app.image.avatar.width}")
    private int width;
    @Value("${app.image.avatar.height}")
    private int height;
    @Value("${app.image.avatar.extensions}")
    private String[] extension;

    @PatchMapping
    // @AuthenticationPrincipal notation che ci consente di recuperari dati dell'utente loggato
    //quando ci logghiamo
    // @Valid vale per il request body @Validated per pathVariable e RequestParam
    public ResponseEntity<?> modifyMe(@RequestBody @Valid ModifyMeRequest request,
                                      @AuthenticationPrincipal UserDetails userDetails){
        if(!userService.modifyMe(userDetails, request))
            return ResponseHandler.generateResponse(HttpStatus.OK, userDetails.getUsername());
        else
            return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT, "Email already in use");
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePwdRequest request,
                                            @AuthenticationPrincipal UserDetails userDetails){
        String msg = userService.changePassword(request, userDetails);
        return ResponseHandler.generateResponse(HttpStatus.OK, msg);
    }

    @PatchMapping("/change-role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> changeRole(@RequestParam String username, @RequestParam Role newRole){
        //l'admin non può cambiare il ruolo  a se stesso ne ad altri admin
        //non può cambiarlo ad utenti GUEST
        return ResponseHandler.generateResponse(HttpStatus.OK, userService.changeRole(username, newRole));
    }

    @PostMapping(value="/add-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //spring mette a diposizione un tipo relativo ai file che vengono caricati
    // che è multipart file per aggiungere il bottonciino di caricamento
    // dobbiamo impostare il parametro consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    //perche di default queste chiamate consumano e producono json
    public ResponseEntity<?> uploadAvatar(@RequestParam MultipartFile file,
                                          @AuthenticationPrincipal UserDetails userDetails){
        if(imageService.checkSize(file, size))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "File too large");
        if(imageService.checkDimensions(file, width, height))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "Wrong width or height");
        if(!imageService.checkExtensions(file, extension))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "File type not allowed");

        return ResponseHandler.generateResponse(HttpStatus.OK, imageService.save(file, userDetails));
    }

    @DeleteMapping(value = "/remove-avatar")
    public ResponseEntity<?> deleteAvatar(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseHandler.generateResponse(HttpStatus.OK, imageService.removeAvatar(userDetails));
    }

    /*
    * @PatchMapping("/remove-avatar")
    public ResponseEntity<?> removeAvatar(@AuthenticationPrincipal  UserDetails userDetails) {
        if(imageService.removeAvatar(userDetails))
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, "User has no avatar");
        return ResponseHandler.generateResponse(HttpStatus.OK, "Avatar successfull deleted");
    }
    * */
}

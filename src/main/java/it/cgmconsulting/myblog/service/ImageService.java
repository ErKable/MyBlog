package it.cgmconsulting.myblog.service;


import it.cgmconsulting.myblog.entity.Avatar;
import it.cgmconsulting.myblog.entity.Section;
import it.cgmconsulting.myblog.entity.SectionImages;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.repository.AvatarRepository;
import it.cgmconsulting.myblog.repository.SectionImagesRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final SectionImagesRepository sectionImagesRepository;
    public boolean checkSize(MultipartFile file, long size){
        if(file.getSize() > size || file.isEmpty())
            return true;
        return false;
    }

    public boolean checkDimensions(MultipartFile file, int width, int height){
        BufferedImage bf = fromMultipartToBufferedImage(file);
        if(bf != null)
            if(bf.getWidth() > width || bf.getHeight() > height)
                return true;
        return false;
    }

    public boolean checkExtensions(MultipartFile file, String[] extensions){
        // non a index 1 ma alla lunghezza
        // oppure getContentType da multipart file
        String[] nomeFile = file.getOriginalFilename().split("\\.");
        return Arrays.asList(extensions).contains(nomeFile[nomeFile.length-1]);
    }

    public boolean checkExtensions2(MultipartFile file, String[] extensions){
        // non a index 1 ma alla lunghezza
        // oppure getContentType da multipart file
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        return Arrays.asList(extensions).contains(ext);
    }


    public String save(MultipartFile file, UserDetails userDetails){

        try {
            Avatar avatar = new Avatar(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            //avatarRepository.save(avatar);
            User user = (User) userDetails;
            if(user.getAvatar() != null)
                avatar.setId(user.getAvatar().getId());

            avatarRepository.save(avatar);

            user.setAvatar(avatar);
            userRepository.save(user);
            return "Your prifile image has been updated";
        } catch (IOException e) {
            return "Error "+e.getMessage();
        }
    }

    //necessario per convertire il file ricevuto in un immagine
    private BufferedImage fromMultipartToBufferedImage(MultipartFile file){
        BufferedImage bf = null;
        try{
            bf = ImageIO.read(file.getInputStream());
            return bf;
        } catch (IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean removeAvatar(UserDetails userDetails){
        //rimozione avatar
        User user = (User) userDetails;
        avatarRepository.deleteById(user.getAvatar().getId());
        //rimozione relazione su user
        user.setAvatar(null);
        userRepository.save(user);

        return true;
    }

    public void uploadImage(MultipartFile file, Section section, String altTag, String path){
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        SectionImages si = new SectionImages();

        si.setSection(section);
        si.setFiletype(ext);
        si.setAltTag(altTag);
        //questa funziona ritorna l'oggeto persistito
        sectionImagesRepository.save(si);
        //upload fisico del file
        //creazione del path per salavare l'img
        Path p = Paths.get(path+si.getId()+"."+ext);
        try {
            Files.write(p, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeImage(int sectionImagesId, String path){
        try {
            SectionImages si = sectionImagesRepository.findById(sectionImagesId).orElseThrow();
            Files.delete(Paths.get(path+sectionImagesId+"."+si.getFiletype()));
            sectionImagesRepository.deleteById(sectionImagesId);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /*
    * public boolean removeAvatar(UserDetails userDetails) {
        User user  = (User) userDetails;
        if(user.getAvatar() != null) {
            avatarRepository.deleteById(user.getAvatar().getId());
            return false;
        }
        return true
    * */
}

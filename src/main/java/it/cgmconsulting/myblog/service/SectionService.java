package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.dto.request.SectionRequest;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Section;
import it.cgmconsulting.myblog.entity.enumerated.SectionType;
import it.cgmconsulting.myblog.repository.SectionImagesRepository;
import it.cgmconsulting.myblog.repository.SectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final PostService postService;
    private final ImageService imageService;
    private final SectionImagesRepository sectionImagesRepository;
    @Transactional
    public String addSection(SectionRequest request){
        Post post = postService.findById(request.getPostId());
        // verifichiamo l'esistenza di Header o Footer per il post
       String msg = checkSectionType(SectionType.valueOf(request.getSectionType()), post);
       if(msg != null)
           return msg;
        // istanziazione section
        Section section = new Section(post, request.getSectionTitle(), request.getSectionContent(),
                request.getSectionOrder(), SectionType.valueOf(request.getSectionType()));
        // persistenza section
        sectionRepository.save(section);
        return "Section added";
    }
    @Transactional
    public String updateSection(SectionRequest request, int sectionId){
        Section section = findById(sectionId);
        Post post = postService.findById(request.getPostId());
        String msg = checkSectionType(SectionType.valueOf(request.getSectionType()), post);
        if(msg != null && ! (SectionType.valueOf(request.getSectionType()).equals(section.getSectionType())))
            return msg;
        section.setSectionTitle(request.getSectionTitle());
        section.setSectionContent(request.getSectionContent());
        section.setSectionType(SectionType.valueOf(request.getSectionType()));
        section.setSectionOrder(request.getSectionOrder());

        post.setPublicationDate(null);
        return "Section updated";
    }

    protected Section findById(int sectionId){
        return sectionRepository.findById(sectionId).orElseThrow();
    }

    private String checkSectionType(SectionType sectionType, Post post){
        String msg = null;
        if( sectionType.equals(SectionType.HE) || sectionType.equals(SectionType.FO)){
            if(sectionRepository.existsBySectionTypeAndPost(sectionType, post))
                msg = "Section "+sectionType.name()+" already exists";
        }
        return msg;
    }

    public boolean addImages(MultipartFile file, int sectionId, String altTag, String path){
        //trovare la section partendo dall'id
        Section section = findById(sectionId);
        //richiamare metodo upload di imageService
        imageService.uploadImage(file, section, altTag, path);
        return true;
    }


    public String removeImage(int sectionImagesId, String path) {
        imageService.removeImage(sectionImagesId, path);
        return "Image has been deleted";
    }

    public String removeSection(int sectionId, String path) {
        String msg = "Section has been deleted";
        List<String> fileToRemove = sectionImagesRepository.filesToDelete(sectionId);
        if(!fileToRemove.isEmpty()){
            // cancellazione records da tabella section images
            sectionImagesRepository.deleteSectionImagesBySectionId(sectionId);
            // cancellazione dei file su cartella
            for(String file : fileToRemove){
                try {
                    Files.delete(Paths.get(path+file));
                } catch (IOException e) {
                    return "An error has occurred while removing images";
                }
            }
        }
        // cancellazione della section
        sectionRepository.deleteById(sectionId);
        return msg;
    }
}

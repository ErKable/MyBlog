package it.cgmconsulting.myblog.service;


import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//qui si scrive la logica di business
@Service // questa notation dice all'app che i metodi nella classe
// vanno inizializzati come componmenti all'avvio dell'app
public class TagService {
    //@Autowired
    //TagRepository tagRepository; dependerncy injection by field

    private final TagRepository tagRepository;
    //dependency injection by constructor: best practice
    public TagService(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }
    public Set<Tag> findAllByVisibleTrueOrderById(){
        return tagRepository.findAllByVisibleTrueOrderById();
    }

    public Set<String> getVisibleTags(){
        return tagRepository.getVisibleTags();
    }

    public Set<String> getVisibleTagsJPQL(){
        return tagRepository.getVisibleTagsJpql();
    }

    public List<Tag> findAll(){
        return tagRepository.findAll();
    };

    public boolean existsById(String id){
        return tagRepository.existsById(id);
    }

    public boolean save(String id){
        if(existsById(id)) return false;
           else{
               tagRepository.save(new Tag(id));
               return false;
        }
    }

    public boolean tagsToAdd(Set<String> tags){
        Set<Tag> newTags = tags.stream().map(Tag::new).collect(Collectors.toSet());
        Set<Tag> oldTags =  new HashSet<>(tagRepository.findAll());
        newTags.removeAll(oldTags);
        if(newTags.isEmpty())
            return false;
            else {
                tagRepository.saveAll(newTags);
                return true;
        }
    }

    public void switchVisibility(Set<Tag> tags){
        tagRepository.saveAll(tags);
    }
}

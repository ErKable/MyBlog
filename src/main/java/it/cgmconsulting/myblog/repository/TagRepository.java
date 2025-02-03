package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;
//Nei repository andiamo ad aggiungere solo i metodi che
//prescindono dai convenzionali
public interface TagRepository extends JpaRepository<Tag, String> {
    //SELECT * FROM tag WHERE visible = true
    //con i metodi derivati (quelli sotto) si possono ottenere solo entità
    //o collection di entità
    Set<Tag> findAllByVisibleTrueOrderById();

    @Query(value = "SELECT id FROM tag t WHERE t.visible = true ORDER BY t.id", nativeQuery = true)
    //SQL nativo: restituisce Entity, Collection di Entity, primitivi/wrapper e loro collection
    Set<String> getVisibleTags();

    //JPQL = Java Persistence Query Language
    @Query(value = "SELECT t.id FROM Tag t WHERE t.visible = true ORDER BY t.id")
    //Essendo Java è case sensitive quindi la tabella deve essere con la lettera maiuscola
    //perche si riferisce all'entità tag. Mappiamo le entità nel progetto
    //non quelle nel db
    Set<String> getVisibleTagsJpql();
    //la query cosi serve ad evita modifiche malevole del set tags
    Set<Tag> findByVisibleTrueAndIdIn(Set<String> tags);

    @Query("SELECT p.tags FROM Post p WHERE p.id = :postId")
    Set<Tag> getPostTags(int postId);

}

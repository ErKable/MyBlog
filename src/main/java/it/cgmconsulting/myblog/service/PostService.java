package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.dto.response.*;
import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Tag;
import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.entity.enumerated.SectionType;
import it.cgmconsulting.myblog.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final SectionRepository sectionRepository;
    private final SectionImagesRepository sectionImagesRepository;
    @Value("${app.image.section.path}")
    private String path;

    public boolean createPost(UserDetails userDetails, String title){
        Post post = new Post((User) userDetails, title);
        postRepository.save(post);
        return true;
    }

    // per evitare due query si poteva fare una query per tornare tutti i dati di un post
    //anche quelli inutili, con fetch type eager, e poi ciclare sulla lista

    public List<PostResponse> getPosts(){
        List<PostResponse> posts = postRepository.getPublishedPosts(LocalDateTime.now());
        //Set<Tag> tags = tagRepository.getPostTags(1);
        List<String> tagsToAdd = new ArrayList<>();
        //tags.forEach(t -> tagsToAdd.add(t.getId()));
        for(int i = 0; i < posts.size(); i++){
            Set<Tag> tags2 = tagRepository.getPostTags(posts.get(i).getPostId());
            tags2.forEach(t -> tagsToAdd.add(t.getId()));
            posts.get(i).setTags(tagsToAdd.toArray(new String[tags2.size()]));
        }
/*        posts.forEach(postResponse -> postResponse.setTags(
                tagRepository.getPostTags(
                        postResponse.getPostId()
                )
        ));*/
        return posts;
    }

    public List<PostResponse> getPostsAdelchi(){
        List<PostResponse> posts = postRepository.getPublishedPosts(LocalDateTime.now());
        List<PostResponse> posts2 = new ArrayList<>();
        for(PostResponse pr : posts){
            String[] tags = postRepository.getTagsByPost(pr.getPostId());
            pr.setTags(tags); posts2.add(pr);
        }
        return posts2; }



    @Transactional
    public void addTags(Set<String> tags, int postId){
        // transformare il ser di stringhe in un set di tag
        Set<Tag> tagsToAdd = tagRepository.findByVisibleTrueAndIdIn(tags);
        //settare il set dei tag sul post
        Post post = findById(postId);
        post.setTags(tagsToAdd);
    }

    public Post findById(int id){
        return postRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void setPublicationDate(LocalDateTime publicationDate, int postId){
/*        Post post = findById(postId);
        post.setPublicationDate(publicationDate);*/
        String msg;
        Post post = findById(postId);
    }

    public List<PostResponse> getPaginatedPosts(int pageNumber, int pageSize, String direction, String sortBy){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostResponse> result = postRepository.getPaginatedPosts(pageable, LocalDateTime.now());
        return result.getContent();
    }

    public List<PostBoxResponse> getBoxes(int pageNumber, int pageSize, String direction, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostBoxResponse> result = postRepository.getBoxes(pageable, LocalDateTime.now(), SectionType.HE, path);
        List<PostBoxResponse> list = new ArrayList<>();
        for(PostBoxResponse pbr : result.getContent()){
            pbr.setTags(postRepository.getTagsByPost(pbr.getPostId()));
            list.add(pbr);
        }
        return list;
    }

    public List<PostResponse> getPostsByKeyword(int pageNumber, int pageSize, String direction, String sortBy, String keyword, boolean isCaseSensitive, boolean isExactMatch) {
        //Creazione ogetto Pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        //Ottengo la lista dei post selezionando i campi dell'oggetto PostKeywordResponse
        Page<PostKeywordResponse> result = postRepository.getPaginatedPublishedPosts(LocalDateTime.now(), pageable, "%"+keyword+"%");
        //La lista dei post che rispondono ai requisiti, i cui attributi sono filtrati secondo PostResponse
        List<PostResponse> selectedPost = new ArrayList<>();

        //Gestione del parametro isCaseSensitive per la keyword
        keyword = (isCaseSensitive) ? keyword : keyword.toLowerCase();
        //Creo un patter regex a seconda che sia necessario o meno un match esatto
        Pattern pattern = Pattern.compile(isExactMatch ? "\\b" + keyword + "\\p{Punct}?\\b" : Pattern.quote(keyword));

        //Itero sui post estratti
        for(PostKeywordResponse pkr: result.getContent()) {
            //Creo un blob di testo che somma tutti i testi su cui fare ricerca nel post
            StringBuilder textBlob = new StringBuilder();
            //Appendo il titolo del testo
            textBlob.append((isCaseSensitive) ? pkr.getPostTitle() + " " : pkr.getPostTitle().toLowerCase() + " ");
            //estraggo le sezioni per il post che sto analizzando
            List<String> sectionsContent = sectionRepository.getSectionsSectionContentByPostId(pkr.getPostId());
            //Le aggiungo al mio oggetto PostKeywordResponse
            pkr.setSectionsContent(sectionsContent);
            //Aggiungo il testo da controllare delle sezioni nel mio blob
            for(String sectionContent: sectionsContent) {
                textBlob.append((isCaseSensitive) ? sectionContent + " " : sectionContent.toLowerCase() + " ");
            }
            //Controllo se il post passa il controllo
            if(pattern.matcher(textBlob.toString()).find())  selectedPost.add(pkr.getPostResponse());
        }
        return selectedPost;
}


    public List<PostResponse> getPostsByTag(int pageNumber, int pageSize, String direction, String sortBy, String tag){
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
        Page<PostResponse> result = postRepository.getPostsByTag(pageable, LocalDateTime.now(), tag);
        return result.getContent();
    }

    protected Post findPublishedPostById(int id, LocalDateTime now){
        return postRepository.findByIdAndPublicationDateIsNotNullAndPublicationDateBefore(id, now).orElseThrow();
    }
    protected Optional<Post> findPublishedPostByIdOpt(int id, LocalDateTime now){
        return postRepository.findByIdAndPublicationDateIsNotNullAndPublicationDateBefore(id, now);
    }

    public PostDetailResponse getPostDetail(int postId) {
        PostDetailResponse pdr = new PostDetailResponse();
        //cereare una PostResponse
        PostResponse postResponse = postRepository.getPublishedPost(LocalDateTime.now(), postId);
        if (postResponse != null) {
            pdr.setPostResponse(postResponse);
            //aggiungere una List<SectionResponse>
            List<SectionResponse> sectionResponses = sectionRepository.getSectionByPostId(postId);
            if (sectionResponses.isEmpty())
                return null;
            List<SectionResponse> sectionWithImages = new ArrayList<>();
            //per ogni SectionResponse associare List<SectionImagesResponse>
            for (SectionResponse sr : sectionResponses) {
                List<SectionImagesResponse> images = sectionImagesRepository.getSectionImages(sr.getId(), path);
                sr.setImages(images);
                sectionWithImages.add(sr);
            }
            pdr.setSections(sectionWithImages);
            return pdr;
        }
        return null;
    }

}

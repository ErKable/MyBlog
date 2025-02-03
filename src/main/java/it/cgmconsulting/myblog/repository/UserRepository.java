package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //Optional aggiunto da Java 8 e funziona da wrapper
    //se trova un risultato il contenitore contienre un oggeto user
    //se non trova nulla e vuoto quindi dilla usiamo il metodo idPresent
    //per verificre se c'é qualcosa
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
    boolean existsByEmailOrUsername(String email, String username);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdIsNot(String email, int id);
}
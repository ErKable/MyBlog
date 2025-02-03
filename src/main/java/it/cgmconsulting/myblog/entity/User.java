package it.cgmconsulting.myblog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.enumerated.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "user_")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class User extends CreationUpdate implements UserDetails{
    //user details e un interfaccia di spring security
    //che gestisce i dati dell'utente
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //auto increment
    private int id;

    @Column(nullable = false, length = 15, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore //questa notazione fa si che se viene generato un
    //json di user la pw non viene inviata
    @Column(nullable = false)
    private String password; // encrypted -> $2a$10$uRjzxWBfvrC5UPDwjpQoV.JsZLl6ClFHZuk9fAYW39T.n1PE021Km

    @Column(length = 50)
    private String firstname;

    @Column(length = 50)
    private String lastname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.GUEST;

    private boolean enabled = false;

    private String bio;

    @OneToOne//(cascade = CascadeType.REMOVE)
    // si può aggiungere on delete set null
    //@OnDelete(action = OnDeleteAction.SET_NULL)
    private Avatar avatar;

    private LocalDateTime bannedUntil;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        // Spring Security utilizza lo username, in questo caso noi useremo l'email
        // così come avviene nella maggior parte dei casi nel mondo reale
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
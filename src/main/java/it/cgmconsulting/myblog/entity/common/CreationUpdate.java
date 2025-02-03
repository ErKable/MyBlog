package it.cgmconsulting.myblog.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass //fa in modo che quando questa viene estesa gli attributi generano colonne sul DB
@Getter @Setter
public class CreationUpdate extends Creation implements Serializable { //serve alle librerie jackson per convertire queste classi in JSON
    @UpdateTimestamp //vale solo per hibernate e lo usiamo solo se usiamo questa libreria per salvare i dati nel DB
    private LocalDateTime updatedAt;
}
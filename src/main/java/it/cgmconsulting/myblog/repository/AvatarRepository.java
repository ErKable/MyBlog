package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

/*
* Per ogni entita creiamo un repository, queste sono interfacce di JPA
* che mettono a disposizioni interfacce per le CRUD
* queste hanno bisogno di parametri che sono
* Il tipo dell'entità e il tipo (wrapper) della PK dell'entità
* *
Per fare le query abbiamo tre modi, JPA, JPQL e SQL nativo
/
 */
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
    //eredità tutti i metodi crud
}

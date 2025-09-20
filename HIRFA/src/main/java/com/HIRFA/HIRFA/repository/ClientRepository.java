package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;//pour utiliser javarepository qui contient les focntion findall..

import java.util.Optional;
import java.util.UUID;//parcque clientid est un uuid

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}

/*
 * c est une interface qui herite de jparepository elle permet d acceder a la
 * base sans ecrire de sql
 * il contient les methodes CRUD pretes save findall
 */
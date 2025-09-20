package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;//pour utiliser javarepository qui contient les focntion findall..

import java.util.Optional;
import java.util.UUID;//parcque clientid est un uuid

public interface CooperativeRepository extends JpaRepository<Cooperative, UUID> {
    Optional<Cooperative> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByBrand(String brand);
}

/*
 * c est une interface qui herite de jparepository elle permet d acceder a la
 * base sans ecrire de sql
 * il contient les methodes CRUD pretes save findall
 */
package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Client> findByUsernameOrEmail(String username, String email);

    @Query("SELECT COUNT(c) > 0 FROM Client c WHERE LOWER(c.username) = LOWER(:username)")
    boolean existsByUsernameIgnoreCase(@Param("username") String username);

    @Query("SELECT COUNT(c) > 0 FROM Client c WHERE LOWER(c.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    /**
     * Search clients by name, email, or address with pagination
     * 
     * @param search   search term (can be null or empty to get all clients)
     * @param pageable pagination information
     * @return page of clients matching the search criteria
     */
    @Query("SELECT c FROM Client c WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "LOWER(c.nom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.prenom) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.adresse) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Client> searchClients(@Param("search") String search, Pageable pageable);
}
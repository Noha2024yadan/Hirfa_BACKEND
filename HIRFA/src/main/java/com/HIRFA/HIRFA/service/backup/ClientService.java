package com.HIRFA.HIRFA.service.backup;

import com.HIRFA.HIRFA.dto.ClientRegistrationDto;
import com.HIRFA.HIRFA.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ClientService {

    Client registerClient(ClientRegistrationDto registrationDto);

    Optional<Client> findById(UUID id);

    Optional<Client> findByEmail(String email);

    Optional<Client> findByUsername(String username);

    Optional<Client> findByUsernameOrEmail(String username, String email);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Page<Client> findAll(Pageable pageable);

    Client update(UUID id, Client client);

    void delete(UUID id);

    void toggleStatus(UUID id);
}

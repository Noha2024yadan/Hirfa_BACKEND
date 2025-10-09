package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.ClientRegistrationDto;
import com.HIRFA.HIRFA.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IClientService {
    Client registerClient(ClientRegistrationDto registrationDto);

    Optional<Client> login(String usernameOrEmail, String password);

    /**
     * Find all clients with optional search filter
     * 
     * @param search   optional search term to filter clients by name, email, or
     *                 address
     * @param pageable pagination information
     * @return page of clients
     */
    Page<Client> findAllClients(String search, Pageable pageable);

    /**
     * Find a client by ID
     * 
     * @param id client ID
     * @return client if found
     */
    Optional<Client> findById(UUID id);

    /**
     * Update client information
     * 
     * @param id        client ID
     * @param clientDto client data to update
     * @return updated client
     */
    Client updateClient(UUID id, ClientRegistrationDto clientDto);

    /**
     * Delete a client by ID
     * 
     * @param id client ID
     */
    void deleteClient(UUID id);
}

package com.HIRFA.HIRFA.service.backup;

import com.HIRFA.HIRFA.dto.ClientRegistrationDto;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.exception.EmailAlreadyExistsException;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.exception.UsernameAlreadyExistsException;
import com.HIRFA.HIRFA.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Client> findAllClients(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return clientRepository.searchClients(search, pageable);
        }
        return clientRepository.findAll(pageable);
    }

    public Client login(String username, String password) {
        Optional<Client> client = clientRepository.findByUsernameOrEmail(username, username);
        if (client.isEmpty() || !passwordEncoder.matches(password, client.get().getMotDePasse())) {
            throw new ResourceNotFoundException("Invalid credentials");
        }
        return client.get();
    }

    @Override
    @Transactional
    public Client registerClient(ClientRegistrationDto registrationDto) {
        if (existsByEmail(registrationDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        if (existsByUsername(registrationDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already taken");
        }

        Client client = new Client();
        client.setNom(registrationDto.getNom());
        client.setPrenom(registrationDto.getPrenom());
        client.setEmail(registrationDto.getEmail());
        client.setUsername(registrationDto.getUsername());
        client.setTelephone(registrationDto.getTelephone());
        client.setMotDePasse(passwordEncoder.encode(registrationDto.getMotDePasse()));
        client.setAdresse(registrationDto.getAdresse());
        client.setDateCreation(LocalDateTime.now());
        client.setStatut(true);

        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public Optional<Client> findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    @Override
    public Optional<Client> findByUsernameOrEmail(String username, String email) {
        return clientRepository.findByUsernameOrEmail(username, email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return clientRepository.existsByUsername(username);
    }

    @Override
    public Page<Client> findAll(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Client update(UUID id, Client updatedClient) {
        Client existingClient = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        existingClient.setNom(updatedClient.getNom());
        existingClient.setPrenom(updatedClient.getPrenom());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setTelephone(updatedClient.getTelephone());
        existingClient.setAdresse(updatedClient.getAdresse());

        return clientRepository.save(existingClient);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Client client = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientRepository.delete(client);
    }

    @Override
    @Transactional
    public void toggleStatus(UUID id) {
        Client client = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        client.setStatut(!client.isStatut());
        clientRepository.save(client);
    }
}

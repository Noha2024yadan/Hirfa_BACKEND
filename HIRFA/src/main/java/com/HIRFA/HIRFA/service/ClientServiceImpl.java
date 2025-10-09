package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.ClientRegistrationDto;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.exception.ValidationException;
import com.HIRFA.HIRFA.repository.ClientRepository;
import com.HIRFA.HIRFA.service.validation.RegistrationValidationService;
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
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private final RegistrationValidationService validationService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
            RegistrationValidationService validationService,
            PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Client registerClient(ClientRegistrationDto registrationDto) {
        // Validate registration data
        validationService.validateRegistration(registrationDto);

        // Check if username or email already exists
        if (clientRepository.existsByUsernameIgnoreCase(registrationDto.getUsername())) {
            throw new ValidationException("Username is already taken!");
        }

        if (clientRepository.existsByEmailIgnoreCase(registrationDto.getEmail())) {
            throw new ValidationException("Email is already in use!");
        }

        // Create and populate new client
        Client client = new Client();
        mapRegistrationFields(registrationDto, client);

        // Save client
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> login(String usernameOrEmail, String password) {
        Optional<Client> client = clientRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (client.isPresent() && passwordEncoder.matches(password, client.get().getMotDePasse())) {
            Client loggedInClient = client.get();
            loggedInClient.setDerniereConnexion(LocalDateTime.now());
            clientRepository.save(loggedInClient);
            return Optional.of(loggedInClient);
        }
        return Optional.empty();
    }

    @Override
    public Page<Client> findAllClients(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return clientRepository.searchClients(search, pageable);
        }
        return clientRepository.findAll(pageable);
    }

    @Override
    public Optional<Client> findById(UUID id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client updateClient(UUID id, ClientRegistrationDto clientDto) {
        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        if (clientDto.getNom() != null)
            existingClient.setNom(clientDto.getNom());
        if (clientDto.getPrenom() != null)
            existingClient.setPrenom(clientDto.getPrenom());

        if (clientDto.getEmail() != null && !clientDto.getEmail().equals(existingClient.getEmail())) {
            if (clientRepository.existsByEmailIgnoreCase(clientDto.getEmail())) {
                throw new ValidationException("Email already in use.");
            }
            existingClient.setEmail(clientDto.getEmail().toLowerCase());
        }

        if (clientDto.getUsername() != null && !clientDto.getUsername().equals(existingClient.getUsername())) {
            if (clientRepository.existsByUsernameIgnoreCase(clientDto.getUsername())) {
                throw new ValidationException("Username already taken.");
            }
            existingClient.setUsername(clientDto.getUsername().toLowerCase());
        }

        if (clientDto.getTelephone() != null)
            existingClient.setTelephone(clientDto.getTelephone());
        if (clientDto.getAdresse() != null)
            existingClient.setAdresse(clientDto.getAdresse());

        if (clientDto.getMotDePasse() != null && !clientDto.getMotDePasse().isEmpty()) {
            existingClient.setMotDePasse(passwordEncoder.encode(clientDto.getMotDePasse()));
        }

        return clientRepository.save(existingClient);
    }

    @Override
    public void deleteClient(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        clientRepository.delete(client);
    }

    private void mapRegistrationFields(ClientRegistrationDto source, Client target) {
        target.setNom(source.getNom());
        target.setPrenom(source.getPrenom());
        target.setEmail(source.getEmail().toLowerCase());
        target.setUsername(source.getUsername().toLowerCase());
        target.setTelephone(source.getTelephone());
        target.setAdresse(source.getAdresse());
        target.setMotDePasse(passwordEncoder.encode(source.getMotDePasse()));
        target.setDateCreation(LocalDateTime.now());
    }
}

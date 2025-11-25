package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.*;
import com.HIRFA.HIRFA.entity.*;
import com.HIRFA.HIRFA.exception.ResourceNotFoundException;
import com.HIRFA.HIRFA.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private CooperativeRepository cooperativeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Object registerUser(Object registrationDto) {

        // COMMON FIELDS
        String username = null;
        String email = null;
        String password = null;

        // MANUAL TYPE DETECTION (SAFE)
        if (registrationDto instanceof ClientRegistrationDto dto) {
            username = dto.getUsername();
            email = dto.getEmail();
            password = dto.getMotDePasse();

            validateUser(username, email);

            Client c = new Client();
            c.setUsername(username);
            c.setEmail(email);
            c.setMotDePasse(encodePassword(password));
            c.setEnabled(true);
            c.setDateCreation(LocalDateTime.now());
            c.setUserType(UserType.CLIENT);

            return clientRepository.save(c);
        }

        else if (registrationDto instanceof DesignerRegistrationDto dto) {
            username = dto.getUsername();
            email = dto.getEmail();
            password = dto.getMotDePasse();

            validateUser(username, email);

            Designer d = new Designer();
            d.setUsername(username);
            d.setEmail(email);
            d.setMotDePasse(encodePassword(password));
            d.setEnabled(true);
            d.setDateCreation(LocalDateTime.now());
            d.setUserType(UserType.DESIGNER);

            return designerRepository.save(d);
        }

        else if (registrationDto instanceof CooperativeRegistrationDto dto) {
            username = dto.getUsername();
            email = dto.getEmail();
            password = dto.getMotDePasse();

            validateUser(username, email);

            Cooperative c = new Cooperative();
            c.setUsername(username);
            c.setEmail(email);
            c.setMotDePasse(encodePassword(password));
            c.setEnabled(true);
            c.setDateCreation(LocalDateTime.now());
            c.setUserType(UserType.COOPERATIVE);

            return cooperativeRepository.save(c);
        }

        throw new RuntimeException("Unknown registration DTO type!");
    }

    private void validateUser(String username, String email) {
        if (existsByUsername(username)) {
            throw new RuntimeException("Username already exists!");
        }
        if (existsByEmail(email)) {
            throw new RuntimeException("Email already exists!");
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return clientRepository.existsByUsername(username)
                || designerRepository.existsByUsername(username)
                || cooperativeRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email)
                || designerRepository.existsByEmail(email)
                || cooperativeRepository.existsByEmail(email);
    }

    @Override
    public Optional<Object> findUserById(UUID userId) {
        return clientRepository.findById(userId).map(c -> (Object) c)
                .or(() -> designerRepository.findById(userId).map(d -> (Object) d))
                .or(() -> cooperativeRepository.findById(userId).map(c -> (Object) c));
    }

    @Override
    public Page<Object> findAllUsers(UserType userType, Boolean enabled, String search, Pageable pageable) {
        List<Object> users = new ArrayList<>();

        if (userType == null || userType == UserType.CLIENT)
            users.addAll(clientRepository.findAll());

        if (userType == null || userType == UserType.DESIGNER)
            users.addAll(designerRepository.findAll());

        if (userType == null || userType == UserType.COOPERATIVE)
            users.addAll(cooperativeRepository.findAll());

        return new PageImpl<>(users, pageable, users.size());
    }

    @Override
    @Transactional
    public Object updateUserStatus(UUID userId, boolean enabled) {

        Optional<Client> client = clientRepository.findById(userId);
        if (client.isPresent()) {
            client.get().setEnabled(enabled);
            return clientRepository.save(client.get());
        }

        Optional<Designer> designer = designerRepository.findById(userId);
        if (designer.isPresent()) {
            designer.get().setEnabled(enabled);
            return designerRepository.save(designer.get());
        }

        Optional<Cooperative> coop = cooperativeRepository.findById(userId);
        if (coop.isPresent()) {
            coop.get().setEnabled(enabled);
            return cooperativeRepository.save(coop.get());
        }

        throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {

        if (clientRepository.existsById(userId)) {
            clientRepository.deleteById(userId);
            return;
        }

        if (designerRepository.existsById(userId)) {
            designerRepository.deleteById(userId);
            return;
        }

        if (cooperativeRepository.existsById(userId)) {
            cooperativeRepository.deleteById(userId);
            return;
        }

        throw new ResourceNotFoundException("User not found with id: " + userId);
    }

    @Override
    public long countAllUsers() {
        return clientRepository.count()
                + designerRepository.count()
                + cooperativeRepository.count();
    }

    @Override
    public long countUsersByType(UserType type) {
        return switch (type) {
            case CLIENT -> clientRepository.count();
            case DESIGNER -> designerRepository.count();
            case COOPERATIVE -> cooperativeRepository.count();
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }

    @Override
    public long countActiveUsersToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        return clientRepository.countByDerniereConnexionAfter(start)
                + designerRepository.countByDerniereConnexionAfter(start)
                + cooperativeRepository.countByDerniereConnexionAfter(start);
    }

    @Override
    public long countNewUsersThisWeek() {
        LocalDateTime start = LocalDate.now().minusDays(7).atStartOfDay();
        return clientRepository.countByDateCreationAfter(start)
                + designerRepository.countByDateCreationAfter(start)
                + cooperativeRepository.countByDateCreationAfter(start);
    }

    @Override
    public Page<Object> searchUsers(String query, Pageable pageable) {

        List<Object> users = new ArrayList<>();
        users.addAll(clientRepository.searchByUsernameOrEmail(query));
        users.addAll(designerRepository.searchByUsernameOrEmail(query));
        users.addAll(cooperativeRepository.searchByUsernameOrEmail(query));

        return new PageImpl<>(users, pageable, users.size());
    }

    @Override
    public Page<UserActivityDto> getRecentActivities(Pageable pageable) {

        List<UserActivityDto> activities = new ArrayList<>();

        clientRepository.findAll().forEach(c -> activities.add(
                new UserActivityDto(
                        c.getUsername(),
                        c.getEmail(),
                        c.getDerniereConnexion(),
                        "LOGIN",
                        "Client logged in")));

        designerRepository.findAll().forEach(d -> activities.add(
                new UserActivityDto(
                        d.getUsername(),
                        d.getEmail(),
                        d.getDerniereConnexion(),
                        "LOGIN",
                        "Designer logged in")));

        cooperativeRepository.findAll().forEach(co -> activities.add(
                new UserActivityDto(
                        co.getUsername(),
                        co.getEmail(),
                        co.getDerniereConnexion(),
                        "LOGIN",
                        "Cooperative logged in")));

        activities.sort((a, b) -> b.getDerniereConnexion().compareTo(a.getDerniereConnexion()));

        return new PageImpl<>(activities, pageable, activities.size());
    }

    protected String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}

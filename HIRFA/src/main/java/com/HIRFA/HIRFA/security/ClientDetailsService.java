package com.HIRFA.HIRFA.security;

import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@Service
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Client not found with username or email: " + usernameOrEmail));

        // Use email and password fields (adjust if your names differ)
        return new User(
                client.getEmail(),
                client.getMotDePasse(),
                new ArrayList<>());
    }
}

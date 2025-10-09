package com.HIRFA.HIRFA.security;

import com.HIRFA.HIRFA.entity.User;
import com.HIRFA.HIRFA.repository.ClientRepository;
import com.HIRFA.HIRFA.repository.DesignerRepository;
import com.HIRFA.HIRFA.repository.CooperativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private DesignerRepository designerRepository;

    @Autowired
    private CooperativeRepository cooperativeRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find user in each repository
        Optional<User> user = findUserByUsernameOrEmail(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username or email: " + username);
        }

        return new CustomUserDetails(user.get());
    }

    private Optional<User> findUserByUsernameOrEmail(String username) {
        // Check clients
        Optional<User> user = clientRepository.findByUsernameOrEmail(username, username).map(c -> (User) c);

        // If not found in clients, check designers
        if (user.isEmpty()) {
            user = designerRepository.findByUsernameOrEmail(username, username).map(d -> (User) d);
        }

        // If still not found, check cooperatives
        if (user.isEmpty()) {
            user = cooperativeRepository.findByUsernameOrEmail(username, username).map(c -> (User) c);
        }

        return user;
    }
}

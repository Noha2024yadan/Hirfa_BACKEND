package com.HIRFA.HIRFA.security;

import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.entity.Cooperative;
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
        Object user = findUserByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));

        if (user instanceof Client) {
            Client client = (Client) user;
            return new CustomUserDetails(client.getId(), client.getUsername(), client.getEmail(),
                    client.getMotDePasse(), "CLIENT", client.isStatut());
        } else if (user instanceof Designer) {
            Designer designer = (Designer) user;
            return new CustomUserDetails(designer.getId(), designer.getUsername(), designer.getEmail(),
                    designer.getMotDePasse(), "DESIGNER", designer.isActive());
        } else if (user instanceof Cooperative) {
            Cooperative coop = (Cooperative) user;
            return new CustomUserDetails(coop.getId(), coop.getUsername(), coop.getEmail(), coop.getMotDePasse(),
                    "COOPERATIVE", coop.isActive());
        } else {
            throw new UsernameNotFoundException("User type not supported");
        }
    }

    private Optional<Object> findUserByUsernameOrEmail(String username) {
        // Check clients
        Optional<Client> client = clientRepository.findByUsernameOrEmail(username, username);
        if (client.isPresent())
            return Optional.of(client.get());

        // Check designers
        Optional<Designer> designer = designerRepository.findByUsernameOrEmail(username, username);
        if (designer.isPresent())
            return Optional.of(designer.get());

        // Check cooperatives
        Optional<Cooperative> coop = cooperativeRepository.findByUsernameOrEmail(username, username);
        if (coop.isPresent())
            return Optional.of(coop.get());

        // Nothing found
        return Optional.empty();
    }
}

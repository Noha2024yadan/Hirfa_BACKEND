package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}

package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CooperativeRepository extends JpaRepository<Cooperative, UUID> {
    Cooperative findByConfirmationCode(String code);
    Cooperative findByEmail(String email);
}

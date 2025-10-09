package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.CooperativeRegistrationDto;
import com.HIRFA.HIRFA.entity.Cooperative;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

public interface CooperativeService {

    Cooperative registerCooperative(CooperativeRegistrationDto registrationDto);

    Optional<Cooperative> findByUsername(String username);  // ✅ Fix

    Cooperative verifyCooperative(UUID cooperativeId, boolean verified);

    List<Cooperative> findAllCooperatives(Boolean verified);

    Object countByStatutVerification(boolean b);

    Object findAllCooperatives(Boolean verified, Pageable pageable);
}

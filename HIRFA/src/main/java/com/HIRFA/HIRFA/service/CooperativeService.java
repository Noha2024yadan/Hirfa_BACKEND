package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.CooperativeDTO;
import com.HIRFA.HIRFA.dto.CooperativeRegistrationDto;
import com.HIRFA.HIRFA.dto.DesignerBasicDTO;
import com.HIRFA.HIRFA.entity.Cooperative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CooperativeService {

    Cooperative registerCooperative(CooperativeRegistrationDto registrationDto);

    Optional<Cooperative> findByUsername(String username);

    Cooperative verifyCooperative(UUID cooperativeId, boolean verified);

    List<Cooperative> findAllCooperatives(Boolean verified);

    Page<Cooperative> findAllCooperatives(Boolean verified, Pageable pageable);

    long countByStatutVerification(String statutVerification);

    Cooperative updateCooperative(UUID cooperativeId, CooperativeDTO dto);

    DesignerBasicDTO getDesignerProfile(UUID designerId);
}

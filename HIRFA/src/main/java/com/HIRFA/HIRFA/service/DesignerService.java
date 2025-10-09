package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.DesignerRegistrationDto;
import com.HIRFA.HIRFA.dto.DesignerUpdateDto;
import com.HIRFA.HIRFA.entity.Designer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface DesignerService {
    Designer registerDesigner(DesignerRegistrationDto registrationDto);
    boolean existsByPortfolio(String portfolio);
    
    /**
     * Find all designers with optional search filter
     * @param search optional search term to filter designers by name, email, or portfolio
     * @param pageable pagination information
     * @return page of designers
     */
    Page<Designer> findAllDesigners(String search, Pageable pageable);

    Optional<Designer> findByUsername(String username);

    Designer updateDesignerProfile(String username, DesignerUpdateDto updateDto);
}



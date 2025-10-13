package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Design;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DesignRepository extends JpaRepository<Design, UUID> {
    List<Design> findByIsReportedTrue();

    Page<Design> findByIsReportedTrue(Pageable pageable);
}

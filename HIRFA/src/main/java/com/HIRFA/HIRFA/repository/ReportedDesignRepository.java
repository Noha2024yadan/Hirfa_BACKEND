package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.ReportedDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReportedDesignRepository extends JpaRepository<ReportedDesign, UUID> {
}

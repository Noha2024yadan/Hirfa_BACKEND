package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DesignRepository extends JpaRepository<Design, UUID> {
}

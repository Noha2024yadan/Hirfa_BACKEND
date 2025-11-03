package com.HIRFA.HIRFA.repository;

import com.HIRFA.HIRFA.entity.Designer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DesignerRepository extends JpaRepository<Designer, UUID> {
}

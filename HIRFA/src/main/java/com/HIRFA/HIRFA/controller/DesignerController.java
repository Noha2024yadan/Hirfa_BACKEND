package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.DesignerDTO;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.service.DesignerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/designer/profile")
public class DesignerController {

    private final DesignerService designerService;

    public DesignerController(DesignerService designerService) {
        this.designerService = designerService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Designer> updateDesigner(
            @PathVariable("id") UUID designerId,
            @RequestBody DesignerDTO dto) {

        Designer updated = designerService.updateDesigner(designerId, dto);
        return ResponseEntity.ok(updated);
    }
}

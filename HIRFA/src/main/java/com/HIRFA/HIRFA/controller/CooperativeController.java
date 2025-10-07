package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.CooperativeDTO;
import com.HIRFA.HIRFA.dto.DesignerBasicDTO;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.service.CooperativeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cooperative")
public class CooperativeController {

    private final CooperativeService cooperativeService;

    public CooperativeController(CooperativeService cooperativeService) {
        this.cooperativeService = cooperativeService;
    }

    @PatchMapping("profile/{id}")
    public ResponseEntity<Cooperative> updateCooperative(
            @PathVariable("id") UUID cooperativeId,
            @RequestBody CooperativeDTO dto) {

        Cooperative updated = cooperativeService.updateCooperative(cooperativeId, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/designer/{id}")
    public ResponseEntity<DesignerBasicDTO> viewDesignerProfile(@PathVariable("id") UUID designerId) {
        DesignerBasicDTO dto = cooperativeService.getDesignerProfile(designerId);
        return ResponseEntity.ok(dto);
    }
}

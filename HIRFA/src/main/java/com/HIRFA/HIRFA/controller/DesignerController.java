package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.services.DesignerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/designers")
public class DesignerController {
    @Autowired
    private DesignerService designerService;

    

    @PostMapping
    public ResponseEntity<Designer> createDesigner(@RequestBody Designer designer) {
        Designer createdDesigner = designerService.createDesigner(designer.getNom(), designer.getEmail());
        return ResponseEntity.ok(createdDesigner);
    }
}

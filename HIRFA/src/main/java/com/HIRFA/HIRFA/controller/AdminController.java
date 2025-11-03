package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @DeleteMapping("/client/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable UUID id) {
        adminService.deleteClient(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/designer/{id}")
    public ResponseEntity<?> deleteDesigner(@PathVariable UUID id) {
        adminService.deleteDesigner(id);
        return ResponseEntity.ok().build();
    }
}

package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.ClientDTO;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.service.AIProfileService;
import com.HIRFA.HIRFA.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("//api/client/profile")
public class ClientController {

    private final ClientService clientService;
    private AIProfileService aiProfileService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PatchMapping("/{id}")
    public Client updateClient(@PathVariable UUID id, @RequestBody ClientDTO dto) {
        return clientService.updateClient(id, dto);
    }
}
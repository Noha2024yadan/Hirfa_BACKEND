package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.ClientDTO;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // PATCH pour mise à jour partielle
    @PatchMapping("/{id}")
    public Client updateClient(@PathVariable UUID id, @RequestBody ClientDTO dto) {
        return clientService.updateClient(id, dto);
    }
}

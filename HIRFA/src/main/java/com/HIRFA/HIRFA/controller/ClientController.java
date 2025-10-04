package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.services.DesignerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
2
@RestController
@RequestMapping("/api/client")
public class ClientController{
    @Autowired
    private ClientService clientService;
    @Autowired
    private AIProfileService aiProfileService;
    @PutMapping("/preferences")
    public ResponseEntity<Client> updatePreferences(
            @RequestBody UserPreferencesRequest request,
            @AuthenticationPrincipal Client currentClient) {
        
        UUID clientId = currentClient.getId();
        
        Client client = clientService.updatePreferences(clientId, request);
       
        Map<String, Object> prefs = Map.of(
            "bio", request.isBio(),
            "local", request.isLocal(),
            "categories", request.getPreferredCategories()
        );
        
        aiProfileService.updateCLientProfile(
            clientId,
            request.getBudgetMin(),
            request.getBudgetMax(),
            prefs,
            client.getCityId(),
            user.getLanguage()
        );
        
        return ResponseEntity.ok(client);
    }
}

package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.dto.ApiResponse;
import com.HIRFA.HIRFA.dto.ClientRegistrationDto;
import com.HIRFA.HIRFA.dto.DesignerRegistrationDto;
import com.HIRFA.HIRFA.dto.CooperativeRegistrationDto;
import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.service.ClientService;
import com.HIRFA.HIRFA.service.DesignerService;
import com.HIRFA.HIRFA.service.CooperativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
@CrossOrigin(origins = "*")
public class RegistrationController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DesignerService designerService;

    @Autowired
    private CooperativeService cooperativeService;

    // ===================== CLIENT =====================
    @PostMapping("/client")
    public ResponseEntity<ApiResponse<Client>> registerClient(@RequestBody ClientRegistrationDto registrationDto) {
        try {
            Client client = clientService.registerClient(registrationDto);
            return ResponseEntity.ok(new ApiResponse<>(client, "Client registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, e.getMessage(), "CLIENT_REGISTRATION_FAILED"));
        }
    }

    // ===================== DESIGNER =====================
    @PostMapping("/designer")
    public ResponseEntity<ApiResponse<Designer>> registerDesigner(
            @RequestBody DesignerRegistrationDto registrationDto) {
        try {
            Designer designer = designerService.registerDesigner(registrationDto);
            return ResponseEntity.ok(new ApiResponse<>(designer, "Designer registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, e.getMessage(), "DESIGNER_REGISTRATION_FAILED"));
        }
    }

    // ===================== COOPERATIVE =====================
    @PostMapping("/cooperative")
    public ResponseEntity<ApiResponse<Cooperative>> registerCooperative(
            @RequestBody CooperativeRegistrationDto registrationDto) {
        try {
            Cooperative cooperative = cooperativeService.registerCooperative(registrationDto);
            return ResponseEntity.ok(new ApiResponse<>(cooperative, "Cooperative registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(null, e.getMessage(), "COOPERATIVE_REGISTRATION_FAILED"));
        }
    }
}

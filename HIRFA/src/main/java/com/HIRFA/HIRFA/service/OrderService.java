package com.HIRFA.HIRFA.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final AIEventService aiEventService;  // Injecter AiEventService pour loguer les événements

    public void completeOrder(UUID clientId, UUID productId, int qty, double price) {
        // Log de l'événement "purchase" pour l'achat du produit
        aiEventService.logPurchase(clientId.toString(), productId.toString(), qty, price, Map.of("source", "web"));
    }
}

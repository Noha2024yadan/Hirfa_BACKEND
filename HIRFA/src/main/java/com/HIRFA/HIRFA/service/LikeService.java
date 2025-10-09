package com.HIRFA.HIRFA.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final AIEventService aiEventService;  // Injecter AiEventService pour loguer les événements

    public void likeProduct(UUID clientId, UUID productId) {
        // Log de l'événement "like"
        aiEventService.logLike(clientId.toString(), productId.toString(), Map.of("source", "web"));
    }
}

package com.HIRFA.HIRFA.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.HIRFA.HIRFA.entity.Panier;
import com.HIRFA.HIRFA.service.CartService;
import com.HIRFA.HIRFA.service.AIEventService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private AIEventService aiEventService;

    @PostMapping("/{clientId}/add")
    public ResponseEntity<Panier> addProduct(
            @PathVariable UUID clientId,
            @RequestParam UUID productId,
            @RequestParam int qty) {
        return ResponseEntity.ok(cartService.addProductToCart(clientId, productId, qty));
    }

    @PutMapping("/{clientId}/update")
    public ResponseEntity<Panier> updateQty(
            @PathVariable UUID clientId,
            @RequestParam UUID productId,
            @RequestParam int qty) {
        return ResponseEntity.ok(cartService.updateQuantity(clientId, productId, qty));
    }

    @DeleteMapping("/{clientId}/remove/{productId}")
    public ResponseEntity<Panier> removeProduct(
            @PathVariable UUID clientId,
            @PathVariable UUID productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(clientId, productId));
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Panier> getCart(@PathVariable UUID clientId) {
        return ResponseEntity.ok(cartService.getCart(clientId));
    }

    @DeleteMapping("/{clientId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable UUID clientId) {
        cartService.clearCart(clientId);
        return ResponseEntity.noContent().build();
    }


}

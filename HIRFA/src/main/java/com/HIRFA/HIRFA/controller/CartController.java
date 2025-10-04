package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.services.DesignerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController{
    @Autowired
    private CartService cartService;
    @Autowired
    private AIEventService aiEventService;
    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
        @RequestBody AddToCartRequest request,
        @AuthentificationPrincipal Client currentClient){
      UUID clientId = getUserIdFromPrincipal(currentClient);
      Cart cart = cartService.addItem(clientId, request.getProductId(), request.getQuantity());

      aiEventService.trackEvent(clientId, request.getProductId(), "add_to_cart");

      return ResponseEntity.ok(cart);
    }
    
}
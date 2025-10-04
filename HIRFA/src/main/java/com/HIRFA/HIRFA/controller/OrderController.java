package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.services.DesignerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private AIEventService aiEventService;

    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(
        @RequestBody CheckoutRequest request,
        @AuthentificationPrincipal Client currentClient){
      UUID clientId = getUserIdFromPrincipal(currentClient);
      Order order = orderService.createOrder(clientId, request);

      for (OrderItem item: order.getItems()){
        aiEventService.trackEvent(clientId, item.getProductId(), "purchase");
      }
      return ResponseEntity.ok(order);
    }
    
}
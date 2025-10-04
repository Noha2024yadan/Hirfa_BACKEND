package com.HIRFA.HIRFA.controller;

import com.HIRFA.HIRFA.entity.Designer;
import com.HIRFA.HIRFA.services.DesignerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController{
    @Autowired
    private ProductService productService;
    @Autowired
    private AIEventService aiEventService;
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(
          @PathVariable UUID id,
          @AuthentificationPrincipal Client currentClient){
      Product product = productService.findById(id);
      if(currentClient != null){
        UUID clientid= getUserIdFromPrincipal(currentClient);
        aiEventService.trackEvent(clientId, id, "view");
      }
        return ResponseEntity.ok(product);
    }
    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeProduct(
            @PathVariable UUID id;
            @AuthentificationPrincipal Client currentClient){
       UUID clientId = getUserIdFromPrincipal(currentClient);
       productService.toggleLike(clientId, id);

       aiEventService.trackEvent(clientId, id, "like");
       return ResponseEntity.ok().build();
    }

}
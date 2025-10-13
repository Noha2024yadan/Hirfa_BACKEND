package com.HIRFA.HIRFA.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.HIRFA.HIRFA.entity.Client;
import com.HIRFA.HIRFA.entity.Panier;
import com.HIRFA.HIRFA.entity.PanierItem;
import com.HIRFA.HIRFA.entity.Product;
import com.HIRFA.HIRFA.repository.PanierRepository;
import com.HIRFA.HIRFA.repository.ProductRepository;
import com.HIRFA.HIRFA.repository.panier_itemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final PanierRepository panierRepository;
    private final panier_itemRepository panierItemRepository;
    private final ProductRepository productRepository;

    public Panier addProductToCart(UUID clientId, UUID productId, int qty) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (product.getStockQuantity() < qty) {
            throw new RuntimeException("Stock insuffisant");
        }

        Panier panier = panierRepository.findByClient_ClientId(clientId)
                .orElseGet(() -> {
                    Panier newPanier = new Panier();
                    Client client = new Client();
                    client.setClientId(clientId);
                    newPanier.setClient(client);
                    return panierRepository.save(newPanier);
                });

        PanierItem item = panierItemRepository.findByPanierAndProduct(panier, product)
                .orElseGet(() -> {
                    PanierItem newItem = new PanierItem();
                    newItem.setPanier(panier);
                    newItem.setProduct(product);
                    newItem.setPrixUnitaire(product.getPrice());
                    newItem.setQuantite(0);
                    return newItem;
                });

        item.setQuantite(item.getQuantite() + qty);
        panier.getItems().add(item);

        panierRepository.save(panier);
        return panier;
    }

    public Panier updateQuantity(UUID clientId, UUID productId, int qty) {
        Panier panier = panierRepository.findByClient_ClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        PanierItem item = panierItemRepository.findByPanierAndProduct(panier, product)
                .orElseThrow(() -> new RuntimeException("Produit non dans le panier"));

        item.setQuantite(qty);
        panierRepository.save(panier);
        return panier;
    }

    public Panier removeProductFromCart(UUID clientId, UUID productId) {
        Panier panier = panierRepository.findByClient_ClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        panier.getItems().removeIf(i -> i.getProduct().getProductId().equals(productId));
        return panierRepository.save(panier);
    }

    public Panier getCart(UUID clientId) {
        return panierRepository.findByClient_ClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Panier vide"));
    }

    public void clearCart(UUID clientId) {
        Panier panier = panierRepository.findByClient_ClientId(clientId)
                .orElseThrow(() -> new RuntimeException("Panier introuvable"));

        panier.getItems().clear();
        panierRepository.save(panier);
    }
}

package com.HIRFA.HIRFA.service;

import com.HIRFA.HIRFA.dto.CooperativeBasicInfoDto;
import com.HIRFA.HIRFA.entity.Cooperative;
import com.HIRFA.HIRFA.repository.CooperativeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// ===== SERVICE POUR LES INFORMATIONS DE COOPÉRATIVES CÔTÉ DESIGNER =====
// Permet aux designers de consulter les informations de base des coopératives

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CooperativeInfoService {

    private final CooperativeRepository cooperativeRepository;

    /**
     * Obtenir les informations de base d'une coopérative
     * @param cooperativeId ID de la coopérative
     * @return Informations de base
     */
    public CooperativeBasicInfoDto getCooperativeBasicInfo(UUID cooperativeId) {
        Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
            .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));

        // Compter les produits de cette coopérative
        // Note: Il faudra ajuster selon la relation entre Cooperative et Product
        // Assumons qu'il y a une relation ou un champ cooperativeId dans Product
        long totalProducts = countProductsByCooperative(cooperativeId);
        long activeProducts = countActiveProductsByCooperative(cooperativeId);

        // Formater la date d'inscription
        String memberSince = cooperative.getDateCreation() != null ? 
            cooperative.getDateCreation().format(DateTimeFormatter.ofPattern("MMMM yyyy")) : 
            "Non disponible";

        return CooperativeBasicInfoDto.builder()
            .cooperativeId(cooperative.getUserId())
            .brand(cooperative.getBrand())
            .description(cooperative.getDescription())
            .adresse(cooperative.getAdresse())
            .email(cooperative.getEmail())
            .telephone(cooperative.getTelephone())
            .statutVerification(cooperative.getStatutVerification())
            .isActive(cooperative.isActive())
            .totalProducts(totalProducts)
            .activeProducts(activeProducts)
            .memberSince(memberSince)
            .build();
    }

    /**
     * Obtenir les informations de toutes les coopératives actives
     * @return Liste des informations de base des coopératives
     */
    public List<CooperativeBasicInfoDto> getAllActiveCooperatives() {
        List<Cooperative> cooperatives = cooperativeRepository.findByEnabled(true);
        
        return cooperatives.stream()
            .map(coop -> getCooperativeBasicInfo(coop.getUserId()))
            .collect(Collectors.toList());
    }

    /**
     * Rechercher des coopératives par nom de marque
     * @param brand Nom de la marque à rechercher
     * @return Liste des coopératives correspondantes
     */
    public List<CooperativeBasicInfoDto> searchCooperativesByBrand(String brand) {
        List<Cooperative> cooperatives = cooperativeRepository
            .findByBrandContainingIgnoreCaseAndEnabled(brand, true);
        
        return cooperatives.stream()
            .map(coop -> getCooperativeBasicInfo(coop.getUserId()))
            .collect(Collectors.toList());
    }

    /**
     * Obtenir les coopératives par ville/région
     * @param city Ville ou région
     * @return Liste des coopératives dans cette zone
     */
    public List<CooperativeBasicInfoDto> getCooperativesByLocation(String city) {
        List<Cooperative> cooperatives = cooperativeRepository
            .findByAdresseContainingIgnoreCaseAndEnabled(city, true);
        
        return cooperatives.stream()
            .map(coop -> getCooperativeBasicInfo(coop.getUserId()))
            .collect(Collectors.toList());
    }

    // Méthodes utilitaires privées
    private long countProductsByCooperative(UUID cooperativeId) {
        // TODO: Ajouter la relation Product-Cooperative dans le futur
        // Pour l'instant, retourner 0 en attendant la mise en place de la relation
        return 0;
    }

    private long countActiveProductsByCooperative(UUID cooperativeId) {
        // TODO: Ajouter la relation Product-Cooperative dans le futur
        // Pour l'instant, retourner 0 en attendant la mise en place de la relation
        return 0;
    }
}
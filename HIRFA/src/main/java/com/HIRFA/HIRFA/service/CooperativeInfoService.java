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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CooperativeInfoService {

        private final CooperativeRepository cooperativeRepository;

        public CooperativeBasicInfoDto getCooperativeBasicInfo(UUID cooperativeId) {

                Cooperative cooperative = cooperativeRepository.findById(cooperativeId)
                                .orElseThrow(() -> new RuntimeException("Coopérative non trouvée"));

                long totalProducts = countProductsByCooperative(cooperativeId);
                long activeProducts = countActiveProductsByCooperative(cooperativeId);

                String memberSince = cooperative.getDateCreation() != null
                                ? cooperative.getDateCreation().format(DateTimeFormatter.ofPattern("MMMM yyyy"))
                                : "Non disponible";

                return CooperativeBasicInfoDto.builder()
                                .cooperativeId(cooperative.getUserId())
                                .brand(cooperative.getName()) // FIXED (Cooperative has no getBrand)
                                .description(cooperative.getDescription()) // FIXED
                                .adresse(cooperative.getAddress()) // FIXED
                                .email(cooperative.getEmail())
                                .telephone(cooperative.getTelephone())
                                .statutVerification(cooperative.getStatutVerification()) // FIXED
                                .isActive(cooperative.isActive())
                                .totalProducts(totalProducts)
                                .activeProducts(activeProducts)
                                .memberSince(memberSince)
                                .build();
        }

        public List<CooperativeBasicInfoDto> getAllActiveCooperatives() {

                List<Cooperative> cooperatives = cooperativeRepository.findByEnabled(true);

                return cooperatives.stream()
                                .map(coop -> getCooperativeBasicInfo(coop.getUserId()))
                                .collect(Collectors.toList());
        }

        public List<CooperativeBasicInfoDto> searchCooperativesByBrand(String brand) {

                List<Cooperative> cooperatives = cooperativeRepository
                                .findByNameContainingIgnoreCaseAndEnabled(brand, true); // FIXED (no brand)

                return cooperatives.stream()
                                .map(coop -> getCooperativeBasicInfo(coop.getUserId()))
                                .collect(Collectors.toList());
        }

        public List<CooperativeBasicInfoDto> getCooperativesByLocation(String city) {

                List<Cooperative> cooperatives = cooperativeRepository
                                .findByAddressContainingIgnoreCaseAndEnabled(city, true); // FIXED

                return cooperatives.stream()
                                .map(coop -> getCooperativeBasicInfo(coop.getUserId()))
                                .collect(Collectors.toList());
        }

        private long countProductsByCooperative(UUID cooperativeId) {
                return 0; // implement later
        }

        private long countActiveProductsByCooperative(UUID cooperativeId) {
                return 0; // implement later
        }
}

package com.HIRFA.HIRFA.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportedDesignAdminView {

    private UUID reportId;
    private String reason;
    private String status;
    private LocalDateTime reportDate;

    private UUID designId;
    private String nomDesign;
    private String description;
    private Double prix;
    private List<String> imageUrls;
}

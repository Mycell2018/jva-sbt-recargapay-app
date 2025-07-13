package com.recargapay.app.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditReadDTO {
    @Schema(example = "User")
    private String entityName;

    @Schema(example = "c0b1fbbb-7de9-4c20-a778-44697991c9a3")
    private UUID entityId;

    @Schema(example = "15")
    private Integer revisionId;

    @Schema(example = "admin@recargapay.com")
    private String username;

    @Schema(example = "2025-07-12T23:00:00Z")
    private Instant timestamp;

    @Schema(example = "ADD")
    private String type;

    @Schema(description = "Raw audit data")
    private Map<String, Object> data;
}

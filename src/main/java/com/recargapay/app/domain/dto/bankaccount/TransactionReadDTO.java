package com.recargapay.app.domain.dto.bankaccount;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReadDTO {

    @Schema(description = "Transaction ID", example = "a45d172f-3e33-412b-b60b-4b1c2bb83a24")
    private String id;

    @Schema(description = "Origin account ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private BankAccountReadDTO account;

    @Schema(description = "Destination account ID (for transfers)", example = "321e4567-e89b-12d3-a456-426614174999")
    private BankAccountReadDTO relatedAccount;

    @Schema(description = "Transaction type", example = "DEPOSIT")
    private String type;

    @Schema(description = "Transaction status", example = "COMPLETED")
    private String status;

    @Schema(description = "Transaction amount", example = "100.00")
    private String amount;

    @Schema(description = "Transaction description", example = "Initial deposit")
    private String description;

    @Schema(description = "Reference ID for related operations", example = "2d2ee5fc-e14e-4bb1-ae60-d6cc2a85e277")
    private String referenceId;

    @Schema(description = "Date and time of transaction", example = "2025-07-12T10:15:30")
    private String createdAt;
}

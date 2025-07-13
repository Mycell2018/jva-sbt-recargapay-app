package com.recargapay.app.controller;

import com.recargapay.app.config.exception.dto.ErrorResponse;
import com.recargapay.app.domain.dto.AuditReadDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/audit")
@Tag(name = "Audit", description = "API for audit logs")
public interface AuditSwagger {

    @Operation(summary = "Get audit by entity ID", description = "Retrieve audit history for an entity")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Success",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = AuditReadDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{entity}/{id}")
    ResponseEntity<List<AuditReadDTO>> getByEntity(@PathVariable String entity, @PathVariable UUID id);

    @Operation(summary = "List all audit entries", description = "Retrieve all audit logs")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Success",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = AuditReadDTO.class)))
    })
    @GetMapping
    ResponseEntity<List<AuditReadDTO>> listAll();
}

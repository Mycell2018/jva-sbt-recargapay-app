package com.recargapay.app.controller;

import com.recargapay.app.config.exception.dto.ErrorResponse;
import com.recargapay.app.domain.dto.user.UserCreateDTO;
import com.recargapay.app.domain.dto.user.UserReadDTO;
import com.recargapay.app.domain.dto.user.UserUpdateDTO;
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
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "API for user management")
public interface UserSwagger {

    @Operation(summary = "Create user", description = "Create a new user")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Created",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = UserReadDTO.class))),
                @ApiResponse(
                        responseCode = "422",
                        description = "Unprocessable Entity",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping
    ResponseEntity<UserReadDTO> create(@RequestBody UserCreateDTO dto);

    @Operation(summary = "Get all users", description = "Retrieve all users")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Success",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = UserReadDTO.class))),
                @ApiResponse(responseCode = "204", description = "No Content")
            })
    @GetMapping
    ResponseEntity<List<UserReadDTO>> read();

    @Operation(summary = "Get user by ID", description = "Retrieve a user by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Success",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = UserReadDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{id}")
    ResponseEntity<UserReadDTO> readById(@PathVariable UUID id);

    @Operation(summary = "Get user by CPF", description = "Retrieve a user by CPF")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Success",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = UserReadDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/cpf/{cpf}")
    ResponseEntity<UserReadDTO> readByCpf(@PathVariable String cpf);

    @Operation(summary = "Update user", description = "Update user information")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Updated",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        schema = @Schema(implementation = UserReadDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PutMapping("/{id}")
    ResponseEntity<UserReadDTO> update(@PathVariable UUID id, @RequestBody UserUpdateDTO dto);

    @Operation(summary = "Delete user", description = "Delete a user by ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Deleted successfully"),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id);
}

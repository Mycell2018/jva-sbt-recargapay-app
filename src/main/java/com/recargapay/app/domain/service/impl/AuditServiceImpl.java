package com.recargapay.app.domain.service.impl;

import com.recargapay.app.domain.dto.AuditReadDTO;
import com.recargapay.app.domain.service.AuditService;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<AuditReadDTO> getAllRevisions() {
        return List.of(
                        query("users_aud", "User"),
                        query("bank_accounts_aud", "BankAccount"),
                        query("account_transactions_aud", "Transaction"),
                        query("account_balances_aud", "AccountBalance"))
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    private List<AuditReadDTO> query(String table, String entityName) {
        String sql = "SELECT a.*, r.id AS rev_id, r.timestamp, r.username " + "FROM sc_recargapay."
                + table + " a " + "JOIN sc_recargapay.revinfo r ON a.rev = r.id "
                + "ORDER BY a.rev DESC";

        return jdbcTemplate.queryForList(sql).stream()
                .map(row -> {
                    Map<String, Object> data = new LinkedHashMap<>(row);
                    return AuditReadDTO.builder()
                            .entityName(entityName)
                            .entityId(toUUID(data.get("id")))
                            .revisionId((Integer) data.get("rev_id"))
                            .username((String) data.get("username"))
                            .timestamp(Instant.ofEpochMilli(((Number) data.get("timestamp")).longValue()))
                            .type(String.valueOf(data.get("revtype")))
                            .data(data)
                            .build();
                })
                .toList();
    }

    @Override
    public List<AuditReadDTO> getRevisions(String entityName, UUID entityId) {
        String table =
                switch (entityName) {
                    case "User" -> "users_aud";
                    case "BankAccount" -> "bank_accounts_aud";
                    case "Transaction" -> "account_transactions_aud";
                    case "AccountBalance" -> "account_balances_aud";
                    default -> throw new IllegalArgumentException("Entidade desconhecida: " + entityName);
                };

        String sql = "SELECT a.*, r.id AS rev_id, r.timestamp, r.username " + "FROM sc_recargapay."
                + table + " a " + "JOIN sc_recargapay.revinfo r ON a.rev = r.id "
                + "WHERE a.id = ? "
                + "ORDER BY a.rev DESC";

        return jdbcTemplate.queryForList(sql, entityId).stream()
                .map(row -> {
                    Map<String, Object> data = new LinkedHashMap<>(row);
                    return AuditReadDTO.builder()
                            .entityName(entityName)
                            .entityId(toUUID(data.get("id")))
                            .revisionId((Integer) data.get("rev_id"))
                            .username((String) data.get("username"))
                            .timestamp(Instant.ofEpochMilli(((Number) data.get("timestamp")).longValue()))
                            .type(String.valueOf(data.get("revtype")))
                            .data(data)
                            .build();
                })
                .toList();
    }

    private UUID toUUID(Object value) {
        try {
            return value != null ? UUID.fromString(value.toString()) : null;
        } catch (Exception e) {
            return null;
        }
    }
}

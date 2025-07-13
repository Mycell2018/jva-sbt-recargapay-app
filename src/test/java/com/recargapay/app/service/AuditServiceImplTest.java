package com.recargapay.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recargapay.app.domain.dto.AuditReadDTO;
import com.recargapay.app.domain.service.impl.AuditServiceImpl;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

class AuditServiceImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuditServiceImpl auditServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllRevisions_shouldReturnList() {
        Map<String, Object> row = Map.of(
                "id",
                UUID.randomUUID().toString(),
                "rev_id",
                1,
                "timestamp",
                System.currentTimeMillis(),
                "username",
                "testuser",
                "revtype",
                0);

        when(jdbcTemplate.queryForList(anyString())).thenReturn(List.of(row));

        List<AuditReadDTO> result = auditServiceImpl.getAllRevisions();

        assertFalse(result.isEmpty());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void getAllRevisions_shouldReturnEmptyWhenNoData() {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(List.of());

        List<AuditReadDTO> result = auditServiceImpl.getAllRevisions();

        assertTrue(result.isEmpty());
    }
}

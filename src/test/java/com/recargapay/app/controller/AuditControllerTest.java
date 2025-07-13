package com.recargapay.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.recargapay.app.domain.dto.AuditReadDTO;
import com.recargapay.app.domain.service.impl.AuditServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class AuditControllerTest {

    @Mock
    private AuditServiceImpl auditServiceImpl;

    @InjectMocks
    private AuditController auditController;

    public AuditControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getByEntity_shouldReturnAuditList() {
        UUID id = UUID.randomUUID();
        List<AuditReadDTO> mockList = Collections.singletonList(AuditReadDTO.builder()
                .entityName("User")
                .entityId(id)
                .revisionId(1)
                .username("tester")
                .build());

        when(auditServiceImpl.getRevisions("User", id)).thenReturn(mockList);

        ResponseEntity<List<AuditReadDTO>> response = auditController.getByEntity("User", id);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("tester", response.getBody().get(0).getUsername());
    }

    @Test
    void listAll_shouldReturnAllAudits() {
        List<AuditReadDTO> mockList = Collections.singletonList(
                AuditReadDTO.builder().entityName("User").username("tester").build());

        when(auditServiceImpl.getAllRevisions()).thenReturn(mockList);

        ResponseEntity<List<AuditReadDTO>> response = auditController.listAll();
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }
}

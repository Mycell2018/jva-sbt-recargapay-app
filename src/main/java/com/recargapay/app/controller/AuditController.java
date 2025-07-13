package com.recargapay.app.controller;

import com.recargapay.app.domain.dto.AuditReadDTO;
import com.recargapay.app.domain.service.AuditService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuditController implements AuditSwagger {

    private final AuditService auditService;

    @Override
    public ResponseEntity<List<AuditReadDTO>> getByEntity(String entity, UUID id) {
        return ResponseEntity.ok(auditService.getRevisions(entity, id));
    }

    @Override
    public ResponseEntity<List<AuditReadDTO>> listAll() {
        return ResponseEntity.ok(auditService.getAllRevisions());
    }
}

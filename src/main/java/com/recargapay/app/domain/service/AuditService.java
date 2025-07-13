package com.recargapay.app.domain.service;

import com.recargapay.app.domain.dto.AuditReadDTO;
import java.util.List;
import java.util.UUID;

public interface AuditService {
    List<AuditReadDTO> getAllRevisions();

    List<AuditReadDTO> getRevisions(String entityName, UUID entityId);
}

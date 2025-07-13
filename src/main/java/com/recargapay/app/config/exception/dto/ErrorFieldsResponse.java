package com.recargapay.app.config.exception.dto;

import java.util.List;

public record ErrorFieldsResponse(
        String type, String title, String detail, String instance, List<ErrorFieldResponse> messages) {}

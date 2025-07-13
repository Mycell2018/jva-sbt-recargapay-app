package com.recargapay.app.config.exception.dto;

public record ErrorResponse(String type, String title, String detail, String instance) {}

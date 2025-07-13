package com.recargapay.app.config.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultMessagesEnum {
    LOG_PRINT_EXCEPTION_INTERNAL_SERVER_ERROR("log.print.exception.internal.server.error"),
    LOG_PRINT_EXCEPTION_BAD_REQUEST("log.print.exception.bad"),
    LOG_PRINT_EXCEPTION_BUSINESS("log.print.exception.business"),
    LOG_PRINT_EXCEPTION_UNAUTHORIZED("log.print.exception.unauthorized"),
    LOG_PRINT_EXCEPTION_FORBIDDEN("log.print.exception.forbidden"),
    LOG_PRINT_EXCEPTION_NO_CONTENT("log.print.exception.no.content"),
    LOG_PRINT_EXCEPTION_UNPROCESSABLE_ENTITY("log.print.exception.unprocessable.entity"),
    LOG_DEFAULT_MESSAGE_MAX("message.default.max"),
    LOG_DEFAULT_MESSAGE_MIN("message.default.min"),
    LOG_DEFAULT_MESSAGE_LENGTH("message.default.length"),
    LOG_DEFAULT_MESSAGE_REQUIRED("message.default.required"),
    LOG_DEFAULT_TITLE_MESSAGE_UNAUTHORIZED("message.default.unauthorized.title"),
    LOG_PRINT_OBJECT("log.print.object"),
    DEFAULT_ERROR_BAD_REQUEST("default.error.dto.is.null"),
    DEFAULT_ERROR_NO_CONTENT("default.error.no.content");

    private String value;
}

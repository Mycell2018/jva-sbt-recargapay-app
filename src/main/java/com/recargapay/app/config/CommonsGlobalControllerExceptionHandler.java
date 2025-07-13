package com.recargapay.app.config;

import com.recargapay.app.config.exception.DefaultMessagesEnum;
import com.recargapay.app.config.exception.RecordAlreadyExistsException;
import com.recargapay.app.config.exception.RecordNotFoundException;
import com.recargapay.app.config.exception.dto.ErrorFieldResponse;
import com.recargapay.app.config.exception.dto.ErrorFieldsResponse;
import com.recargapay.app.config.exception.dto.ErrorResponse;
import com.recargapay.app.config.messages.MessageUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestControllerAdvice
@RequiredArgsConstructor
public class CommonsGlobalControllerExceptionHandler {

    private MessageSourceAccessor accessor;

    private static final String MAX = "max";

    private static final String MIN = "min";

    public String getMessage(String code) {
        return MessageUtil.getMessage(code);
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConversion(RuntimeException ex) {
        var req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var uri = req.getRequestURI();
        var type = ServletUriComponentsBuilder.fromContextPath(req).toUriString()
                + "/api/v1/problems/record-already-exists";
        var errorResponse = new ErrorResponse(type, "Bad Request", ex.getMessage(), uri);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleRecordAlreadyExists(RecordAlreadyExistsException ex) {
        var req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var uri = req.getRequestURI();
        var type = ServletUriComponentsBuilder.fromContextPath(req).toUriString()
                + "/api/v1/problems/record-already-exists";
        var errorResponse = new ErrorResponse(type, "Record Already Exists", ex.getMessage(), uri);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFound(RecordNotFoundException ex) {
        var req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var uri = req.getRequestURI();
        var type = ServletUriComponentsBuilder.fromContextPath(req).toUriString() + "/api/v1/problems/record-not-found";
        var errorResponse = new ErrorResponse(type, "Record Not Found", ex.getMessage(), uri);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        var uri = req.getRequestURI();
        var type = ServletUriComponentsBuilder.fromContextPath(req).toUriString()
                + "/api/v1/problems/field-validation-errors";

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        List<ErrorFieldResponse> listErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            if (errorMessage.charAt(0) == '{' && errorMessage.charAt(errorMessage.length() - 1) == '}') {
                errorMessage = this.getMessage(errorMessage.substring(1, errorMessage.length() - 1));
            }

            if (fieldError.getCode().equalsIgnoreCase(MAX)) {
                errorMessage = createErrorMessageForMax(fieldError, errorMessage);
            } else if (fieldError.getCode().equalsIgnoreCase(MIN)) {
                errorMessage = createErrorMessageForMin(fieldError, errorMessage);
            } else if (fieldError.getCode().equals("Length")) {
                errorMessage = createErrorMessageForLength(fieldError, errorMessage);
            } else if (fieldError.getCode().equals("NotNull")
                    || fieldError.getCode().equals("NotEmpty")) {
                errorMessage = this.getMessage(DefaultMessagesEnum.LOG_DEFAULT_MESSAGE_REQUIRED.getValue())
                        .replace("{0}", fieldName);
            }

            listErrors.add(new ErrorFieldResponse(fieldName, errorMessage));
        });

        return new ResponseEntity<>(
                new ErrorFieldsResponse(type, "field-validation-errors", "Some fields are invalid!", uri, listErrors),
                status);
    }

    private String createErrorMessageForLength(FieldError fieldError, String errorMessage) {
        Object[] fieldErrorArguments = fieldError.getArguments();
        if (fieldErrorArguments != null && fieldErrorArguments.length >= 3) {
            errorMessage = this.getMessage(DefaultMessagesEnum.LOG_DEFAULT_MESSAGE_LENGTH.getValue())
                    .replace("{0}", fieldError.getField());

            int max = Integer.parseInt(fieldErrorArguments[1].toString());
            int min = Integer.parseInt(fieldErrorArguments[2].toString());

            errorMessage = errorMessage.replace("{min}", String.valueOf(min)).replace("{max}", String.valueOf(max));
        }

        return errorMessage;
    }

    private String createErrorMessageForMin(FieldError fieldError, String errorMessage) {
        Object[] fieldErrorArguments = fieldError.getArguments();
        errorMessage = this.getMessage(DefaultMessagesEnum.LOG_DEFAULT_MESSAGE_MIN.getValue())
                .replace("{0}", fieldError.getField());

        if (fieldErrorArguments != null && fieldErrorArguments.length >= 2 && errorMessage.contains(MIN)) {
            Integer reference = Integer.parseInt(fieldErrorArguments[1].toString());
            errorMessage = errorMessage.replace("{min}", reference.toString()).replace("{s}", reference > 1 ? "s" : "");
        }

        return errorMessage;
    }

    private String createErrorMessageForMax(FieldError fieldError, String errorMessage) {
        Object[] fieldErrorArguments = fieldError.getArguments();
        errorMessage = this.getMessage(DefaultMessagesEnum.LOG_DEFAULT_MESSAGE_MAX.getValue())
                .replace("{0}", fieldError.getField());

        if (fieldErrorArguments != null && fieldErrorArguments.length >= 2 && errorMessage.contains(MAX)) {
            Integer reference = Integer.parseInt(fieldErrorArguments[1].toString());
            errorMessage = errorMessage.replace("{max}", reference.toString()).replace("{s}", reference > 1 ? "s" : "");
        }

        return errorMessage;
    }
}

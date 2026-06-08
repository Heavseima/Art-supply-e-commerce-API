package com.artshop.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Central translation of domain and framework exceptions into RFC 7807
 * {@link ProblemDetail} responses. No raw stack traces or ad-hoc JSON
 * ever leave the application boundary through here.
 *
 * <p>Extends {@link ResponseEntityExceptionHandler} so that Spring MVC's own
 * framework exceptions (malformed JSON, unknown routes, unsupported methods,
 * missing params, etc.) are also rendered as ProblemDetail bodies in our house
 * style rather than leaking default messages.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BASE_PROBLEM_TYPE = "https://api.artshop.com/problems/";

    // ------------------------------------------------------------------
    // Domain exceptions
    // ------------------------------------------------------------------

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage(), "resource-not-found");
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ProblemDetail handleInsufficientStock(InsufficientStockException ex) {
        return build(HttpStatus.CONFLICT, "Insufficient Stock", ex.getMessage(), "insufficient-stock");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, "Invalid Request", ex.getMessage(), "invalid-request");
    }

    /**
     * Database constraint breaches surface as 409 Conflict: a duplicate unique
     * value (e.g. slug/name) on create/update, or deleting a category that still
     * has products referencing it (FK RESTRICT).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return build(
                HttpStatus.CONFLICT,
                "Data Conflict",
                "The request conflicts with existing data: a duplicate unique value, "
                        + "or a referenced record that is still in use.",
                "data-conflict");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unhandled exception while processing request", ex);
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred while processing the request.",
                "internal-error");
    }

    // ------------------------------------------------------------------
    // Spring MVC framework exceptions (overrides from ResponseEntityExceptionHandler)
    // ------------------------------------------------------------------

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = build(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                "One or more fields failed validation.",
                "validation-error");

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        problem.setProperty("errors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = build(
                HttpStatus.BAD_REQUEST,
                "Malformed Request Body",
                "The request body is missing or could not be parsed as valid JSON.",
                "malformed-request-body");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = build(
                HttpStatus.BAD_REQUEST,
                "Missing Request Parameter",
                "Required request parameter '%s' of type '%s' is missing."
                        .formatted(ex.getParameterName(), ex.getParameterType()),
                "missing-request-parameter");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = build(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method Not Allowed",
                "HTTP method '%s' is not supported for this endpoint.".formatted(ex.getMethod()),
                "method-not-allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = build(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported Media Type",
                "Content type '%s' is not supported for this endpoint."
                        .formatted(ex.getContentType()),
                "unsupported-media-type");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).headers(headers).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problem = build(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                "No endpoint exists at the requested path.",
                "resource-not-found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(problem);
    }

    // ------------------------------------------------------------------
    // Shared builder
    // ------------------------------------------------------------------

    private ProblemDetail build(HttpStatus status, String title, String detail, String typeSlug) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create(BASE_PROBLEM_TYPE + typeSlug));
        problem.setProperty("timestamp", OffsetDateTime.now());
        return problem;
    }
}

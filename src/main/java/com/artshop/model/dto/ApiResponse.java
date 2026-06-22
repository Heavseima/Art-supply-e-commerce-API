package com.artshop.model.dto;

import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

/**
 * Immutable, standardized envelope wrapping every SUCCESSFUL REST response.
 *
 * <p>This is the success-path counterpart to RFC 7807 {@code ProblemDetail}:
 * controllers wrap their payload in this record, while the global
 * {@code GlobalExceptionHandler} continues to emit {@code ProblemDetail} for
 * every error path. The two concerns are intentionally kept separate -
 * {@code ApiResponse} must never be used to represent an error.
 *
 * <p>Wrapping happens exclusively at the controller layer; services and mappers
 * remain ignorant of it and keep returning bare domain DTOs.
 *
 * @param status    HTTP status code mirrored from the {@code ResponseEntity}
 * @param message   human-readable description of the outcome
 * @param payload   the actual returned object or list ({@code null} for
 *                  no-content style operations)
 * @param timestamp server-side instant the response envelope was created
 * @param <T>       type of the wrapped payload
 */
public record ApiResponse<T>(
        int status,
        String message,
        T payload,
        OffsetDateTime timestamp
) {

    /**
     * Full-control factory: caller supplies the exact status and message.
     */
    public static <T> ApiResponse<T> of(HttpStatus status, String message, T payload) {
        return new ApiResponse<>(status.value(), message, payload, OffsetDateTime.now());
    }

    /**
     * Convenience factory for a {@code 200 OK} success carrying a payload.
     */
    public static <T> ApiResponse<T> success(T payload, String message) {
        return of(HttpStatus.OK, message, payload);
    }

    /**
     * Convenience factory for a {@code 201 Created} success carrying the
     * newly created resource.
     */
    public static <T> ApiResponse<T> created(T payload, String message) {
        return of(HttpStatus.CREATED, message, payload);
    }

    /**
     * Convenience factory for a successful operation that produces no payload
     * (e.g. a delete). Mirrors a {@code 200 OK} with a descriptive message and
     * a {@code null} payload, allowing the envelope to remain consistent even
     * where a bare endpoint would historically have returned {@code 204}.
     */
    public static ApiResponse<Void> message(String message) {
        return of(HttpStatus.OK, message, null);
    }
}
//
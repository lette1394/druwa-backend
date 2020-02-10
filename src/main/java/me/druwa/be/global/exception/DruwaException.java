package me.druwa.be.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DruwaException extends RuntimeException {
    private final DruwaExceptionExplain druwaExceptionExplain;

    private final HttpStatus httpStatus;

    public static DruwaException badRequest(final String message) {
        return new DruwaException(DruwaExceptionExplain.create(message), HttpStatus.BAD_REQUEST);
    }

    public static DruwaException badRequest() {
        return badRequest("");
    }

    public static DruwaException unauthorized(final String message) {
        return new DruwaException(DruwaExceptionExplain.create(message), HttpStatus.UNAUTHORIZED);
    }

    public static DruwaException unauthorized() {
        return unauthorized("");
    }

    public DruwaException appendExplain(final Object actual) {
        druwaExceptionExplain.append("???", actual.toString());
        return this;
    }

    public DruwaException errorCode(final ErrorCode errorCode) {
        druwaExceptionExplain.setErrorCode(errorCode);
        return this;
    }

    public DruwaException appendExplain(final Object expected, final Object actual) {
        druwaExceptionExplain.append(expected.toString(), actual.toString());
        return this;
    }

    ResponseEntity<?> toResponseEntity() {
        return ResponseEntity.status(httpStatus)
                             .body(druwaExceptionExplain.toResponse());
    }
}

package me.druwa.be.global.controller;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import me.druwa.be.global.exception.ExceptionResponse;
import me.druwa.be.global.exception.UnauthorizedException;
import me.druwa.be.log.LoggingUtils;

@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final List<String> errors = ex.getBindingResult()
                                      .getFieldErrors()
                                      .stream()
                                      .map(e -> String.format("[%s] %s, but [%s]",
                                                              e.getField(),
                                                              e.getDefaultMessage(),
                                                              e.getRejectedValue()))
                                      .collect(Collectors.toList());

        final ExceptionResponse body = ExceptionResponse.builder()
                                                        .errors(errors)
                                                        .message("bind error")
                                                        .build();


        return ResponseEntity.badRequest()
                             .body(body);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex,
                                                         final HttpHeaders headers,
                                                         final HttpStatus status,
                                                         final WebRequest request) {
        final List<String> errors = ex.getBindingResult()
                                      .getFieldErrors()
                                      .stream()
                                      .map(e -> String.format("[%s] %s, but [%s]",
                                                              e.getField(),
                                                              e.getDefaultMessage(),
                                                              e.getRejectedValue()))
                                      .collect(Collectors.toList());

        final ExceptionResponse body = ExceptionResponse.builder()
                                                        .errors(errors)
                                                        .message("bind error")
                                                        .build();


        return ResponseEntity.badRequest()
                             .body(body);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(final ConversionNotSupportedException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        return super.handleConversionNotSupported(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex,
                                                        final HttpHeaders headers,
                                                        final HttpStatus status,
                                                        final WebRequest request) {
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        if (ex.getCause() instanceof InvalidFormatException) {
            final InvalidFormatException ex1 = (InvalidFormatException) ex.getCause();

            final String body = ex1.getPath()
                                   .stream()
                                   .map(path -> String.format("%s[%s] cannot be converted into [%s]",
                                                              path.getFieldName(),
                                                              ex1.getValue(),
                                                              ex1.getTargetType())).collect(Collectors.joining());

            return ResponseEntity.badRequest()
                                 .body(ExceptionResponse.builder()
                                                        .errors(Collections.singletonList(body))
                                                        .message("conversion error")
                                                        .build());
        }

        return ResponseEntity.badRequest().build();

    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(final Exception ex,
                                                             final Object body,
                                                             final HttpHeaders headers,
                                                             final HttpStatus status,
                                                             final WebRequest request) {
        LoggingUtils.dumpThrowable(ex);
        if (ex instanceof NoSuchElementException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .build();
        }
        if (ex.getCause() instanceof NoSuchElementException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler({ NoSuchElementException.class, UnauthorizedException.class })
    public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request) throws Exception {
        LoggingUtils.dumpThrowable(ex);

        if (ex instanceof NoSuchElementException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .build();
        }
        if (ex instanceof UnauthorizedException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .build();
    }
}

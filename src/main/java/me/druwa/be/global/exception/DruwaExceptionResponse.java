package me.druwa.be.global.exception;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DruwaExceptionResponse {
    private final String message;
    private final long code;
    private final String error;
    private final List<String> errors;
}

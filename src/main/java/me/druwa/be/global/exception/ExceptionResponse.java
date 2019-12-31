package me.druwa.be.global.exception;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {
    private final String message;
    private final List<String> errors;
}

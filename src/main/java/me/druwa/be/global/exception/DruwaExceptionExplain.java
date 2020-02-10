package me.druwa.be.global.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(staticName = "create")
public class DruwaExceptionExplain {
    private final String message;
    private final List<Tuple> explains = new ArrayList<>();
    @Setter
    private ErrorCode errorCode = ErrorCode.NO_ERROR_CODE;

    @Override
    public String toString() {
        return String.format("%s %s", message, explains.stream()
                                                       .map(Tuple::toString)
                                                       .collect(Collectors.joining(",\n")));
    }

    public DruwaExceptionExplain append(final String expected, final String actual) {
        explains.add(Tuple.of(expected, actual));
        return this;
    }

    DruwaExceptionResponse toResponse() {
        return DruwaExceptionResponse.builder()
                                     .message(message)
                                     .code(errorCode.getCode())
                                     .error(errorCode.toString())
                                     .errors(explains.stream()
                                                     .map(Tuple::toString)
                                                     .collect(Collectors.toList()))
                                     .build();
    }

    @RequiredArgsConstructor(staticName = "of")
    private static class Tuple {
        private final String expected;
        private final String actual;

        @Override
        public String toString() {
            return String.format("expected:[%s], but actual:[%s]", expected, actual);
        }
    }
}

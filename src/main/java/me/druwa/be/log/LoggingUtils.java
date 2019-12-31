package me.druwa.be.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class LoggingUtils {
    public static final String DEFAULT_RESPONSE_TOKEN_HEADER = "druwa-debug-token";
    public static final String DEFAULT_MDC_UUID_TOKEN_KEY = "__trace_id";

    private static final String PREFIX = "@@@";

    public static void dumpThrowable(final Throwable throwable) {
        log.error("{}", formatThrowable(throwable));
    }

    public static String formatThrowable(final Throwable throwable) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(baos);
        throwable.printStackTrace(ps);
        ps.close();

        final String debugToken = MDC.get(DEFAULT_MDC_UUID_TOKEN_KEY);
        final String collect = Arrays.stream(baos.toString().split("\n"))
                                     .map(str -> format(debugToken, str))
                                     .map(str -> str.replace("\t", "\t\t"))
                                     .collect(Collectors.joining("\n"));

        return decorate(debugToken) + collect + decorate(debugToken);
    }

    private static String format(final String debugToken, final String str) {
        return String.format("%s>%s%s", debugToken, PREFIX, str);
    }

    private static String decorate(final String debugToken) {
        return String.format("\n%s>%s\n", debugToken, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }
}

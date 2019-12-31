package me.druwa.be.domain.test;

import java.util.Arrays;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import me.druwa.be.domain.test.service.TestService;
import me.druwa.be.log.LoggingUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final TestService testService;

    @PostMapping(value = "/customer")
    public ResponseEntity<?> saveCustomerPage(@Valid @RequestBody Customer customer) {
        log.info("{}", customer);
        return ResponseEntity.ok().header("druwa-test", "111").build();
    }

    @PostMapping(value = "/customer2")
    public ResponseEntity<?> saveCustomer2(@Valid @RequestBody Customer customer) {
        log.info("{}", customer);
        return ResponseEntity.status(HttpStatus.OK)
                             .header("druwa-test", "111")
                             .body("hellow world!");
    }

    @GetMapping(value = "/json/{id}")
    public ResponseEntity<?> json(@PathVariable int id) {
        log.debug("{}", "Hello world");

        final Person person = Person.of("jaeeunna",
                                        id,
                                        false,
                                        Arrays.asList("read books", "watching TV"),
                                        Lists.newArrayList());
        return ResponseEntity.ok(person);
    }

    @GetMapping(value = "/ex")
    public ResponseEntity<?> ex() {
        log.debug("{}", "Hello world");
        throw new RuntimeException();
    }

    @GetMapping(value = "/ex2")
    public ResponseEntity<?> ex2() {
        log.debug("{}", "Hello world");

        String logggg = "this is loggggg";
        testService.m5()
                   .exceptionally(throwable -> {
                       try {
                           Thread.sleep(2000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                       log.error(LoggingUtils.formatThrowable(throwable));
                       log.error("{}", logggg);
                       return null;
                   });
        m1();
        return ResponseEntity.ok("kkkk");
    }

    private void m1() {
        log.error("{}", "Hello world111");
        m2();
    }

    private void m2() {
        log.error("{}", "Hello world222");
        m3();
    }

    private void m3() {
        log.error("{}", "Hello world333");
        m4();
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> aa(RuntimeException e) {
        LoggingUtils.dumpThrowable(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    void m4() {
        log.error("{}", "Hello world444");
        throw new CustomException();
    }

    public static class CustomException extends RuntimeException {
    }
}

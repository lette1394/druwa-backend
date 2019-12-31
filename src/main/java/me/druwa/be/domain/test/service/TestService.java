package me.druwa.be.domain.test.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import me.druwa.be.domain.test.TestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestService {

    @Async
    public CompletableFuture<?> m5() {
        log.error("{}", "Hello world555");
        throw new TestController.CustomException();
    }
}

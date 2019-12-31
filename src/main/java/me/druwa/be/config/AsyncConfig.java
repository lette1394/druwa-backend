package me.druwa.be.config;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import me.druwa.be.log.LoggingUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setQueueCapacity(20);
        executor.setMaxPoolSize(30);
        executor.setThreadNamePrefix("async-");
        executor.initialize();

        return new HandlingExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (e, method, params) -> log.error(LoggingUtils.formatThrowable(e));
    }

    private static class HandlingExecutor implements AsyncTaskExecutor {
        private ThreadPoolTaskExecutor executor;

        public HandlingExecutor(ThreadPoolTaskExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void execute(Runnable task) {
            executor.execute(createWrappedRunnable(task));
        }

        @Override
        public void execute(Runnable task, long startTimeout) {
            executor.execute(createWrappedRunnable(task), startTimeout);
        }

        @Override
        public Future<?> submit(Runnable task) {
            return executor.submit(createWrappedRunnable(task));
        }

        @Override
        public <T> Future<T> submit(final Callable<T> task) {
            return executor.submit(createCallable(task));
        }

        private <T> Callable<T> createCallable(final Callable<T> task) {
            final Map<String, String> callerMdcContext = MDC.getCopyOfContextMap();
            return () -> {
                MDC.setContextMap(callerMdcContext);
                return task.call();
            };
        }

        private Runnable createWrappedRunnable(final Runnable task) {
            final Map<String, String> callerMdcContext = MDC.getCopyOfContextMap();
            return () -> {
                MDC.setContextMap(callerMdcContext);
                task.run();
            };
        }
    }
}
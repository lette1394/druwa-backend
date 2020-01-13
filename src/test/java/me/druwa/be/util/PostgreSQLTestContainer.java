package me.druwa.be.util;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ContextConfiguration(initializers = PostgreSQLTestContainer.Initializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostgreSQLTestContainer {
    @Container
    protected static PostgreSQLContainer db = new PostgreSQLContainer("postgres:12.1-alpine")
                                                      .withDatabaseName("testcontainer_"
                                                                                + ThreadLocalRandom.current()
                                                                                                   .nextInt(1000))
                                                      .withUsername("test_user")
                                                      .withPassword("test_password");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + db.getJdbcUrl(),
                                  "spring.datasource.username=" + db.getUsername(),
                                  "spring.datasource.password=" + db.getPassword())
                              .applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}

package testContainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractionIntegrationTest.Initializer.class)
public class AbstractionIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        static MySQLContainer<?> mySQL = new MySQLContainer<>("mysql:8.0.28");
        static KafkaContainer kafka = new KafkaContainer(
                DockerImageName.parse("apache/kafka-native:3.8.0")
                        .asCompatibleSubstituteFor("apache/kafka")
        );

        static GenericContainer<?> greenMail = new GenericContainer<>(DockerImageName.parse("greenmail/standalone:latest"));


        private static void startContainers() {
            Startables.deepStart(Stream.of(mySQL, kafka, greenMail)).join();


        }


        private Map<String, Object> createConnectionConfiguration() {
            Map<String, Object> config = new HashMap<>();


            config.put("spring.datasource.url", mySQL.getJdbcUrl());
            config.put("spring.datasource.username", mySQL.getUsername());
            config.put("spring.datasource.password", mySQL.getPassword());
            config.put("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");


            config.put("SECRET_KEY", "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4");
            config.put("EXPIRE_LENGTH", "3600000");


            config.put("MERCADO_PAGO_PUBLIC_KEY", "test_public_key");
            config.put("MERCADO_PAGO_ACCESS_TOKEN", "test_access_token");
            config.put("MERCADO_PAGO_WEBHOOK_SECRET", "test_webhook_secret");
            config.put("TEST_MAIL", "test@example.com");
            config.put("NROK_URL", "https://test-tunnel.ngrok.io");


            config.put("spring.mail.host", "127.0.0.1");
            config.put("spring.mail.port", "3025");
            config.put("spring.mail.username", "duke");
            config.put("spring.mail.password", "springboot");
            config.put("spring.mail.protocol", "smtp");
            config.put("spring.mail.test-connection", "false");

            config.put("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());

            return config;
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

            startContainers();


            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            MapPropertySource testContainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration());

            environment.getPropertySources().addFirst(testContainers);
        }
    }
}

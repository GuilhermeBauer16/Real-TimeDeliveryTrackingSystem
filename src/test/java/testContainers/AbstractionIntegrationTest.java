package testContainers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Stream;

@ContextConfiguration(initializers = AbstractionIntegrationTest.Initializer.class)
public class AbstractionIntegrationTest {

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


        static MySQLContainer<?> mySQL = new MySQLContainer<>("mysql:8.0.28");


        private static void startContainers() {
            Startables.deepStart(Stream.of(mySQL)).join();


        }


        private static DataSource createDataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(mySQL.getJdbcUrl());
            dataSource.setUsername(mySQL.getUsername());
            dataSource.setPassword(mySQL.getPassword());
            return dataSource;
        }


        private Map<String, String> createConnectionConfiguration() {
            return Map.of("spring.datasource.url", mySQL.getJdbcUrl(),
                    "spring.datasource.username", mySQL.getUsername(),
                    "spring.datasource.password", mySQL.getPassword(),
                    "SECRET_KEY", "jxgEQe.XHuPq8VdbyYFNkAN.dudQ0903YUn4",
                    "EXPIRE_LENGTH", "3600000");
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

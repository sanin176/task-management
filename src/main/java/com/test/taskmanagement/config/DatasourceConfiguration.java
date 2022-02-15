package com.test.taskmanagement.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories
public class DatasourceConfiguration extends AbstractR2dbcConfiguration {

    @Value("${database.host}")
    private String host;
    @Value("${database.port}")
    private Integer port;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${database.database}")
    private String database;
    @Value("${database.schema}")
    private String schema;

    @Value("${database.pool.maxIdleTimeInMinutes}")
    private Integer maxIdleTimeInMinutes;
    @Value("${database.pool.maxSize}")
    private Integer maxSize;

    @Bean
    public ConnectionFactory connectionFactory() {
        PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .database(database)
                .schema(schema)
                .build();

        ConnectionPoolConfiguration poolConfiguration =
                ConnectionPoolConfiguration.builder(new PostgresqlConnectionFactory(configuration))
                        .maxIdleTime(Duration.ofMinutes(maxIdleTimeInMinutes == null ? 5 : maxIdleTimeInMinutes))
                        .initialSize(2)
                        .maxSize(maxSize == null ? 5 : maxSize)
                        .build();

        return new ConnectionPool(poolConfiguration);
    }

    // here need add security that get email
    @Bean
    ReactiveAuditorAware<String> auditorAware() {
        return () -> Mono.just("system");
    }

}

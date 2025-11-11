package com.ruoyi.framework.r2dbc;

import com.ruoyi.framework.security.ReactiveSecurityUtils;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Mono;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableR2dbcAuditing
@EnableR2dbcRepositories(basePackages = "com.ruoyi.**.repository")
public class R2dbcDataSourceConfig implements ReactiveAuditorAware<String> {

    @Bean
    @ConfigurationProperties("spring.r2dbc")
    public ConnectionFactory r2dbcConnectionFactory() {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "mysql")
                .option(HOST, "localhost")
                .option(PORT, 3306)
                .option(USER, "root")
                .option(PASSWORD, "password")
                .option(DATABASE, "ry-vue")
                .build());
    }

    @Override
    public Mono<String> getCurrentAuditor() {
        return ReactiveSecurityUtils.getUsername();
    }

}
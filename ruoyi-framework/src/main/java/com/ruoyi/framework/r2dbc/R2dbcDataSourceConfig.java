package com.ruoyi.framework.r2dbc;

import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.R2DBCQueryFactory;
import com.querydsl.r2dbc.SQLTemplates;
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
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
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

    @Bean
    public R2DBCConnectionProvider provider(ConnectionFactory r2dbcConnectionFactory) {
        return () -> Mono.from(r2dbcConnectionFactory.create());
    }

    @Bean
    public R2DBCQueryFactory queryFactory(R2DBCConnectionProvider provider) {
        return new R2DBCQueryFactory(SQLTemplates.DEFAULT, provider);
    }

    @Bean
    public R2dbcTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Override
    public Mono<String> getCurrentAuditor() {
        return ReactiveSecurityUtils.getUsername();
    }

}
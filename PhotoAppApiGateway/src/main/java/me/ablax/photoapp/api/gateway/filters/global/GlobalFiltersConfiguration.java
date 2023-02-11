package me.ablax.photoapp.api.gateway.filters.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GlobalFiltersConfiguration {

    final Logger logger = LoggerFactory.getLogger(GlobalFiltersConfiguration.class);

    @Bean
    public GlobalFilter secondPreFilter() {
        return (exchange, chain) -> {
            logger.info("Second Pre Filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("My second postFilter executed");
            }));
        };
    }

    @Bean
    public GlobalFilter thirdPreFilter() {
        return (exchange, chain) -> {
            logger.info("Second third Filter");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("My third postFilter executed");
            }));
        };
    }


}

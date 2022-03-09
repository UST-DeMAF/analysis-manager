package ust.tad.analysismanager.config;

import org.springframework.amqp.core.HeadersExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Bean
    public HeadersExchange headers() {
        return new HeadersExchange("tad.analysistasks");
    } 
}

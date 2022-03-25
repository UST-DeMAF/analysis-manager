package ust.tad.analysismanager.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Value("${messaging.analysistasks.exchange.name}")
    private String exchangeName;

    @Value("${messaging.analysistaskresponsequeue.name}")
    private String analysisTaskResponseQueueName;

    @Bean
    public HeadersExchange headers() {
        return new HeadersExchange(exchangeName);
    } 

    @Bean
    public Queue pluginQueue() {
        return new Queue("pluginQueue", false);
    }

    @Bean
    public Binding binding(HeadersExchange exchange, Queue queue) {
        Map<String, Object> headerValues = Map.of("technology", "Bash", "analysisType", "STATIC");
        return BindingBuilder.bind(queue)
            .to(exchange)
            .whereAll(headerValues).match();
    }
}

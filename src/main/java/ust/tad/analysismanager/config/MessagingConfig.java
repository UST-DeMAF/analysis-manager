package ust.tad.analysismanager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Value("${messaging.analysistask.request.exchange.name}")
    private String analysisTaskRequestExchangeName;

    @Value("${messaging.analysistask.response.exchange.name}")
    private String analysisTaskResponseExchangeName;

    @Value("${messaging.analysistask.response.queue.name}")
    private String analysisTaskResponseQueueName;

    /**
     * Used to add AMQP entities at runtime.
     */
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public HeadersExchange analysisTaskRequestExchange() {
        return new HeadersExchange(analysisTaskRequestExchangeName, true, false);
    } 

    @Bean
    public DirectExchange analysisTaskResponseExchange() {
        return new DirectExchange(analysisTaskResponseExchangeName, true, false);
    }     

    @Bean
    public Queue analysisTaskResponseQueue() {
        return new Queue(analysisTaskResponseQueueName, true, false, false);
    }

    /**
     * Create a Binding between the analysisTaskResponseExchange and the analysisTaskResponseQueue
     * with the routing key being the name of the analysisTaskResponseQueue
     */
    @Bean
    public Binding analysisTaskResponseQueueBinding(DirectExchange analysisTaskResponseExchange, Queue analysisTaskResponseQueue) {
        return BindingBuilder.bind(analysisTaskResponseQueue)
            .to(analysisTaskResponseExchange)
            .with(analysisTaskResponseQueueName);
    }

}
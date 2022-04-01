package ust.tad.analysismanager.plugin;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PluginRegistrationService {

    @Autowired
    private PluginRepository pluginRepository;

    @Autowired
    private HeadersExchange analysisTaskRequestExchange;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Value("${messaging.analysistask.response.queue.name}")
    private String responseQueueName;
    
    @Value("${messaging.analysistask.response.exchange.name}")
    private String responseExchangeName;

    public PluginRegistrationResponse registerPlugin(PluginRegistrationRequest pluginRegistrationRequest){
        String requestQueueName = createRequestQueueName(pluginRegistrationRequest);

        Queue requestQueue = new Queue(requestQueueName, true, false, false);

        Map<String, Object> routingKeys = Map.of(
            "technology", pluginRegistrationRequest.getTechnology(), 
            "analysisType", pluginRegistrationRequest.getAnalysisType().toString());
        
        Binding requestBinding = BindingBuilder.bind(requestQueue)
            .to(analysisTaskRequestExchange)
            .whereAll(routingKeys).match();
        
        rabbitAdmin.declareQueue(requestQueue);
        rabbitAdmin.declareBinding(requestBinding);

        Plugin plugin = new Plugin();
        plugin.setTechnology(pluginRegistrationRequest.getTechnology());
        plugin.setAnalysisType(pluginRegistrationRequest.getAnalysisType());
        plugin.setQueueName(requestQueueName);

        pluginRepository.save(plugin);

        PluginRegistrationResponse pluginRegistrationResponse = new PluginRegistrationResponse();
        pluginRegistrationResponse.setRequestQueueName(requestQueueName);
        pluginRegistrationResponse.setResponseQueueName(responseQueueName);
        pluginRegistrationResponse.setResponseExchangeName(responseExchangeName);

        return pluginRegistrationResponse;    
    }


    private String createRequestQueueName(PluginRegistrationRequest pluginRegistrationRequest) {
        return String.format("%s%s", 
            pluginRegistrationRequest.getTechnology(), 
            pluginRegistrationRequest.getAnalysisType().toString());
    }

}

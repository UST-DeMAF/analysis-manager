package ust.tad.analysismanager.plugin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger LOG =
      LoggerFactory.getLogger(PluginRegistrationService.class);

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

    /**
     * Registers a new Plugin.
     * Creates a queue for the new plugin and persists information about the plugin 
     * in the configurations database
     * Then, a PluginRegistrationResponse is created and returned. 
     * 
     * @param pluginRegistrationRequest
     * @return
     */
    public PluginRegistrationResponse registerPlugin(PluginRegistrationRequest pluginRegistrationRequest){
        //LOG.info("Registering plugin for technology "+pluginRegistrationRequest.getTechnology()+" and analysis type "+pluginRegistrationRequest.getAnalysisType().toString());
        String requestQueueName = createRequestQueueName(pluginRegistrationRequest);
        createRequestQueue(requestQueueName, pluginRegistrationRequest);        

        Plugin plugin = new Plugin();
        plugin.setTechnology(pluginRegistrationRequest.getTechnology());
        plugin.setAnalysisType(pluginRegistrationRequest.getAnalysisType());
        plugin.setQueueName(requestQueueName);
        pluginRepository.save(plugin);

        PluginRegistrationResponse pluginRegistrationResponse = new PluginRegistrationResponse();
        pluginRegistrationResponse.setRequestQueueName(requestQueueName);
        pluginRegistrationResponse.setResponseExchangeName(responseExchangeName);

        return pluginRegistrationResponse;    
    }


    private String createRequestQueueName(PluginRegistrationRequest pluginRegistrationRequest) {
        return String.format("%s%s", 
            pluginRegistrationRequest.getTechnology(), 
            pluginRegistrationRequest.getAnalysisType().toString());
    }

    /**
     * Creates a request queue for the plugin with a binding to the analysisTaskRequestExchange.
     * The binding uses a routing key that is based on the technology and analysisType of the plugin.
     * Through registering the queue and the binding at the rabbitAdmin, both entities are created 
     * at the message broker.
     * 
     * @param requestQueueName
     * @param pluginRegistrationRequest
     */
    private void createRequestQueue(String requestQueueName, PluginRegistrationRequest pluginRegistrationRequest) {
        Queue requestQueue = new Queue(requestQueueName, true, false, false);

        Map<String, Object> routingKeys = Map.of(
            "technology", pluginRegistrationRequest.getTechnology(), 
            "analysisType", pluginRegistrationRequest.getAnalysisType().toString());
        
        Binding requestBinding = BindingBuilder.bind(requestQueue)
            .to(analysisTaskRequestExchange)
            .whereAll(routingKeys).match();
        
        rabbitAdmin.declareQueue(requestQueue);
        rabbitAdmin.declareBinding(requestBinding);
    }

}

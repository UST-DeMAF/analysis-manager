package ust.tad.analysismanager.analysistask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalysisTaskSender {
    
    private static final Logger LOG =
      LoggerFactory.getLogger(AnalysisTaskSender.class);
    
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private HeadersExchange analysisTaskRequestExchange;
    
    /**
     * Sends a message to start the analysis of a deployment model to the analysisTaskRequestExchange.
     * Creates an entity of type AnalysisTaskStartRequest and adds it to the body of the message as a JSON String.
     * It contains all the necessary information for the plugin to start analyzing.
     * The message is routed to the desired plugin based on the message headers.
     * 
     * @param analysisTask
     * @throws JsonProcessingException
     * @throws AmqpException
     */
    public void send(AnalysisTask analysisTask) throws JsonProcessingException, AmqpException {
        LOG.info("Sending AnalysisTask: "+analysisTask.toString());
        
        AnalysisTaskStartRequest analysisTaskStartRequest = new AnalysisTaskStartRequest(
            analysisTask.getTaskId(), 
            analysisTask.getTransformationProcessId(), 
            analysisTask.getCommands(), 
            analysisTask.getLocations());        
        
        ObjectMapper objectMapper = new ObjectMapper();

        Message message = MessageBuilder
            .withBody(objectMapper.writeValueAsString(analysisTaskStartRequest).getBytes())
            .setContentType(MessageProperties.CONTENT_TYPE_JSON)
            .setHeader("analysisType", analysisTask.getAnalysisType())
            .setHeader("technology", analysisTask.getTechnology())
            .setHeader("formatIndicator", "AnalysisTaskStartRequest")
            .build();
            
        template.convertAndSend(analysisTaskRequestExchange.getName(), "", message);
    }
}

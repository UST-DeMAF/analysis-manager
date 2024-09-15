package ust.tad.analysismanager.analysistaskresponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import ust.tad.analysismanager.transformationprocess.TransformationProcessService;

public class AnalysisTaskResponseReceiver {

  @Autowired private MessageConverter jsonMessageConverter;

  @Autowired private TransformationProcessService transformationProcessService;

  @RabbitListener(queues = "#{analysisTaskResponseQueue.name}")
  public void receive(Message message) {
    try {
      switch (message.getMessageProperties().getHeader("formatIndicator").toString()) {
        case "AnalysisTaskResponse":
          receiveAnalysisTaskResponse(message);
          break;
        case "EmbeddedDeploymentModelAnalysisRequest":
          receiveEmbeddedDeploymentModelAnalysisRequest(message);
          break;
        default:
          break;
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  private void receiveAnalysisTaskResponse(Message message) {
    ObjectMapper mapper = new ObjectMapper();

    AnalysisTaskResponse analysisTaskResponse =
        mapper.convertValue(jsonMessageConverter.fromMessage(message), AnalysisTaskResponse.class);
    transformationProcessService.handleAnalysisTaskResponse(analysisTaskResponse);
  }

  private void receiveEmbeddedDeploymentModelAnalysisRequest(Message message) {
    ObjectMapper mapper = new ObjectMapper();

    EmbeddedDeploymentModelAnalysisRequest embeddedDeploymentModelAnalysisRequest =
        mapper.convertValue(
            jsonMessageConverter.fromMessage(message),
            EmbeddedDeploymentModelAnalysisRequest.class);
    transformationProcessService.handleEmbeddedDeploymentModelAnalysisRequest(
        embeddedDeploymentModelAnalysisRequest);
  }
}

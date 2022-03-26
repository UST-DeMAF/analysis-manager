package ust.tad.analysismanager.analysistaskresponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class AnalysisTaskResponseReceiver {

    private static final Logger LOG =
      LoggerFactory.getLogger(AnalysisTaskResponseReceiver.class);

    @RabbitListener(queues = "#{analysisTaskResponseQueue.name}")
    public void receiveAnalysisTaskResponse(AnalysisTaskResponse analysisTaskResponse) {
        LOG.info("received AnalysisTaskResponse: " + analysisTaskResponse.toString());
    }
    
}

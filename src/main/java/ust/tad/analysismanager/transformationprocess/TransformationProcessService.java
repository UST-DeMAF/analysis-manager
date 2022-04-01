package ust.tad.analysismanager.transformationprocess;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ust.tad.analysismanager.analysistask.AnalysisTask;
import ust.tad.analysismanager.analysistask.AnalysisTaskService;
import ust.tad.analysismanager.analysistask.Location;
import ust.tad.analysismanager.plugin.PluginService;
import ust.tad.analysismanager.shared.AnalysisType;

@Service
public class TransformationProcessService {

    @Autowired
    private PluginService pluginService;

    @Autowired
    private AnalysisTaskService analysisTaskService;

    @Autowired
    private ModelsService modelsService;

    public String startTransformationProcess(String technology, URL locationURL, List<String> commands) {
        UUID transformationProcessId = UUID.randomUUID();        
        AnalysisType analysisType = AnalysisType.STATIC;

        String pluginQueueName = pluginService.getQueueNameForPlugin(technology, analysisType);

        AnalysisTask analysisTask = analysisTaskService.createAnalysisTask(transformationProcessId, technology, analysisType, locationURL, commands, pluginQueueName);

        Location location = new Location();
        location.setUrl(locationURL);

        modelsService.initializeTechnologySpecificDeploymentModel(transformationProcessId, technology, commands, location);

        //initialize technology-agnostic edmm model
        //create and send AnalysisTaskStartRequest


            
        /*
        try {
            analysisTaskSender.send(analysisTask);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AmqpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //String key = analysisTask.getTechnology() + "." + analysisTask.getAnalysisType().toString();
        String key = "";
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(analysisTask);
        return String.format("Key: %s   Message: %s", key, message);
        //return String.format("Transform %s deployment model at %s using commands %s", technology, location, command);
        */


        return "process started";
    }
    
}

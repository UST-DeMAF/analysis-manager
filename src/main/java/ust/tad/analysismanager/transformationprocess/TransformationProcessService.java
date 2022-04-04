package ust.tad.analysismanager.transformationprocess;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ust.tad.analysismanager.analysistask.AnalysisTask;
import ust.tad.analysismanager.analysistask.AnalysisTaskSender;
import ust.tad.analysismanager.analysistask.AnalysisTaskService;
import ust.tad.analysismanager.analysistask.Location;
import ust.tad.analysismanager.plugin.Plugin;
import ust.tad.analysismanager.plugin.PluginException;
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

    @Autowired
    AnalysisTaskSender analysisTaskSender;

    /**
     * Starts the transformation process.
     * This contains the following steps:
     * 1. Determine the plugin for the initial analysis task.
     * 2. Create and persist the initial analysis task.
     * 3. Initialize a technology-specific and technology-agnostic deployment model in the models service.
     * 4. Create an AnalysisTaskStartRequest and send it to the analysis task request queue of the determined plugin.
     * 5. Respond to the user.
     * 
     * @param technology
     * @param locationURL
     * @param commands
     * @return the response to the user as a String.
     */
    public String startTransformationProcess(String technology, URL locationURL, List<String> commands) {
        UUID transformationProcessId = UUID.randomUUID();        
        AnalysisType analysisType = AnalysisType.STATIC;

        Plugin plugin;
        try {
            plugin = pluginService.getPluginByTechnologyAndAnalysisType(technology, analysisType);
        } catch (PluginException pluginException) {
            pluginException.printStackTrace();
            return String.format("Could not start transformation process because an error occured: %s", pluginException.getMessage());
        }
        AnalysisTask analysisTask = analysisTaskService.createAnalysisTask(
            transformationProcessId, technology, analysisType, locationURL, commands, plugin.getId());

        Location location = new Location();
        location.setUrl(locationURL);
        modelsService.initializeTechnologySpecificDeploymentModel(transformationProcessId, technology, commands, location);
        modelsService.initializeTechnologyAgnosticDeploymentModel(transformationProcessId);

        try {
            analysisTaskSender.send(analysisTask);
        } catch (JsonProcessingException | AmqpException e) {
            e.printStackTrace();
            return String.format("Could not start transformation process because an error occured: %s", e.getMessage());
        }

        return "Transformation process started!";
    }
    
}

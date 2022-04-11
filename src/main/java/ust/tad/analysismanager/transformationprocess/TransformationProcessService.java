package ust.tad.analysismanager.transformationprocess;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.jline.terminal.Terminal;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ust.tad.analysismanager.analysistask.AnalysisTask;
import ust.tad.analysismanager.analysistask.AnalysisTaskSender;
import ust.tad.analysismanager.analysistask.AnalysisTaskService;
import ust.tad.analysismanager.analysistask.Location;
import ust.tad.analysismanager.analysistaskresponse.AnalysisTaskResponse;
import ust.tad.analysismanager.analysistaskresponse.EmbeddedDeploymentModelAnalysisRequest;
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

    @Autowired
    Terminal terminal;

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

        Plugin plugin;
        try {
            plugin = pluginService.getPluginByTechnology(technology);
        } catch (PluginException pluginException) { 
            pluginException.printStackTrace();
            return String.format("Could not start transformation process because an error occured: %s", pluginException.getMessage());
        }
        AnalysisTask analysisTask = analysisTaskService.createAnalysisTask(
            transformationProcessId, technology, plugin.getAnalysisType(), locationURL, commands, plugin.getId());

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

    /**
     * Handle an EmbeddedDeploymentModelAnalysisRequest.
     * A new AnalysisTask is created based on the EmbeddedDeploymentModelAnalysisRequest.
     * 
     * @param embeddedDeploymentModelAnalysisRequest
     */
    public void handleEmbeddedDeploymentModelAnalysisRequest(EmbeddedDeploymentModelAnalysisRequest embeddedDeploymentModelAnalysisRequest) {
        analysisTaskService.createAnalysisTaskFromEmbeddedDeploymentModelAnalysisRequest(embeddedDeploymentModelAnalysisRequest);  
    }

    /**
     * Handle an AnalysisTaskResponse.
     * Update the task based on the sucess flag.
     * Then determine the next task to run.
     * If the former task used static analysis technique, create a new dynamic task, 
     * search for a plugin and send an AnalysisTaskStartRequest.
     * If all this failes, look if other tasks wait for execution. 
     * 
     * @param analysisTaskResponse
     */
    public void handleAnalysisTaskResponse(AnalysisTaskResponse analysisTaskResponse) {
        AnalysisTask analysisTask = analysisTaskService.updateStatusFromAnalysisTaskResponse(analysisTaskResponse);
        Plugin plugin;
        if (analysisTask.getAnalysisType() == AnalysisType.STATIC) {
            try {
                plugin = pluginService.getPluginByTechnologyAndAnalysisType(analysisTask.getTechnology(), AnalysisType.DYNAMIC);
                AnalysisTask newTask = analysisTaskService.createDynamicTaskFromStaticTask(analysisTask, plugin.getId());                
                sendAnalysisTask(newTask);
            } catch (PluginException e) {
                e.printStackTrace();
            }
        }        
        runNextWaitingTask();
    }

    /**
     * Run the next AnalysisTask with status == AnalysisStatus.WAITING.
     * When no waiting plugin remains the function for finishing the transformation process is called.
     * Else, search for a plugin. 
     * If no plugin can be found, try the next waiting task.
     * If a plugin can be found update the analysis task and send a request.
     * 
     */
    private void runNextWaitingTask() {
        while(analysisTaskService.areTasksWaiting()) {
            AnalysisTask analysisTask = analysisTaskService.getOneWaitingAnalysisTask();

            Plugin plugin;
            try {
                plugin = pluginService.getPluginByTechnology(analysisTask.getTechnology());
            } catch (PluginException pluginException) { 
                pluginException.printStackTrace();
                analysisTaskService.updateStatusToFailed(analysisTask);
                continue;
            }
            AnalysisTask updatedTask = analysisTaskService.updateStatusToRunning(analysisTask, plugin.getId(), plugin.getAnalysisType());
            sendAnalysisTask(updatedTask);
        }
        finishTransformationProcess();
    }

    /**
     * Send a AnalysisTaskStartRequest based on a given AnalysisTask.
     * If an error occures, set the task to failed and look if other tasks wait for execution.
     * 
     * @param analysisTask
     */
    private void sendAnalysisTask(AnalysisTask analysisTask) {
        try {
            analysisTaskSender.send(analysisTask);
        } catch (JsonProcessingException | AmqpException e) {
            e.printStackTrace();
            analysisTaskService.updateStatusToFailed(analysisTask);
            runNextWaitingTask();
        }
    }

    private void finishTransformationProcess() {
        //todo get result from models service
        terminal.writer().println("Transformation process finished!");
        terminal.flush();
    }

    

    
}

package ust.tad.analysismanager.api;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;

import ust.tad.analysismanager.analysistask.AnalysisTaskService;
import ust.tad.analysismanager.analysistask.TransformationProcessNotExistingException;
import ust.tad.analysismanager.plugin.Plugin;
import ust.tad.analysismanager.plugin.PluginException;
import ust.tad.analysismanager.plugin.PluginService;
import ust.tad.analysismanager.transformationprocess.ModelsService;
import ust.tad.analysismanager.transformationprocess.TransformationProcessService;

@RestController
@RequestMapping("demaf")
public class AnalysisManagerController {

    @Autowired
    private TransformationProcessService transformationProcessService;

    @Autowired
    private AnalysisTaskService analysisTaskService;

    @Autowired
    private ModelsService modelsService;

    @Autowired
    private PluginService pluginService;

    /**
     * Start the transformation process by supplying a technology-specific deployment model as input.
     * 
     * @param technologySpecificDeploymentModel a technology-specific deployment model.
     * @throws ResponseStatusException if the transformation process cannot be started.
     * @return the id of the newly started process as a UUID.
     */
    @PostMapping(value = "/transform", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UUID> transform(@RequestBody TechnologySpecificDeploymentModel technologySpecificDeploymentModel) {
        try {
            UUID transformationProcessId = transformationProcessService.startTransformationProcess(technologySpecificDeploymentModel);
            return new ResponseEntity<>(transformationProcessId, HttpStatus.ACCEPTED);
        } catch (JsonProcessingException | AmqpException | PluginException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not start transformation process because an error occured.", e);
        }
    }

    /**
     * Get the status of the transformation process.
     * If the transformation process is finished, add the transformation result to the response.
     *  
     * @param transformationProcessId the id of the transformation process.
     * @throws ResponseStatusException if no transformation process with the given id exists.
     * @return the status of the transformation process with an optional result.
     */
    @GetMapping(value = "/status/{transformationProcessId}", produces = "application/json")
    public ResponseEntity<ProcessStatusMessage> getStatusByTransformationProcessId(@PathVariable UUID transformationProcessId) {
        ProcessStatusMessage processStatusMessage = new ProcessStatusMessage();
        processStatusMessage.setTransformationProcessId(transformationProcessId);

        try {
            if(Boolean.FALSE.equals(analysisTaskService.areTasksWaitingOrRunning(transformationProcessId))) {
                processStatusMessage.setIsFinished(true);
                processStatusMessage.setResult(modelsService.getResult(transformationProcessId));
            } else {            
                processStatusMessage.setIsFinished(false);
            }
        } catch (TransformationProcessNotExistingException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return new ResponseEntity<>(processStatusMessage, HttpStatus.OK);
    }

    /**
     * Get a list of currently registered plugins.
     * It only returns the names of the plugins, based on the supported deployment technology.
     * 
     * @return the list of currently registered plugins.
     */
    @GetMapping(value = "/plugins", produces = "application/json")
    public ResponseEntity<RegisteredPluginsResponse> getPlugins() {
        List<String> pluginNames = pluginService.getAllPlugins().stream().map(Plugin::getTechnology).collect(Collectors.toList());
        return new ResponseEntity<>(new RegisteredPluginsResponse(pluginNames), HttpStatus.OK);        
    }
    
}

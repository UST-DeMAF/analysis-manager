package ust.tad.analysismanager.transformationprocess;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ust.tad.analysismanager.analysistask.*;
import ust.tad.analysismanager.analysistaskresponse.AnalysisTaskResponse;
import ust.tad.analysismanager.analysistaskresponse.EmbeddedDeploymentModelAnalysisRequest;
import ust.tad.analysismanager.api.TechnologySpecificDeploymentModel;
import ust.tad.analysismanager.plugin.Plugin;
import ust.tad.analysismanager.plugin.PluginException;
import ust.tad.analysismanager.plugin.PluginService;
import ust.tad.analysismanager.shared.AnalysisType;

@Service
public class TransformationProcessService {

  private static final Logger LOG = LoggerFactory.getLogger(TransformationProcessService.class);

  @Autowired private PluginService pluginService;

  @Autowired private AnalysisTaskService analysisTaskService;

  @Autowired private ModelsService modelsService;

  @Autowired private AnalysisTaskSender analysisTaskSender;

  /**
   * Starts the transformation process. This contains the following steps: 1. Determine the plugin
   * for the initial analysis task. 2. Create and persist the initial analysis task. 3. Initialize a
   * technology-specific and technology-agnostic deployment model in the models service. 4. Create
   * an AnalysisTaskStartRequest and send it to the analysis task request queue of the determined
   * plugin.
   *
   * @param tsdm
   * @return the id of the started transformation process.
   * @throws PluginException
   * @throws JsonProcessingException
   * @throws AmqpException
   */
  public UUID startTransformationProcess(TechnologySpecificDeploymentModel tsdm)
      throws PluginException, JsonProcessingException, AmqpException {
    UUID transformationProcessId = UUID.randomUUID();

    String technology = tsdm.getTechnology();
    Plugin plugin = pluginService.getPluginByTechnology(technology);
    AnalysisTask analysisTask =
        analysisTaskService.createAnalysisTask(
            transformationProcessId,
            tsdm.getTechnology(),
            plugin.getAnalysisType(),
            tsdm.getLocationURL(),
            tsdm.getCommands(),
            tsdm.getOptions(),
            plugin.getId());

    Location location = new Location();
    location.setUrl(tsdm.getLocationURL());

    if (!technology.equals("layout-pipeline")) {
      modelsService.initializeTechnologySpecificDeploymentModel(
          transformationProcessId, tsdm.getTechnology(), tsdm.getCommands(), location);
      modelsService.initializeTechnologyAgnosticDeploymentModel(transformationProcessId);
    }

    analysisTaskSender.send(analysisTask);

    return transformationProcessId;
  }

  /**
   * Handle an EmbeddedDeploymentModelAnalysisRequest. A new AnalysisTask is created based on the
   * EmbeddedDeploymentModelAnalysisRequest.
   *
   * @param embeddedDeploymentModelAnalysisRequest
   */
  public void handleEmbeddedDeploymentModelAnalysisRequest(
      EmbeddedDeploymentModelAnalysisRequest embeddedDeploymentModelAnalysisRequest) {
    analysisTaskService.createAnalysisTaskFromEmbeddedDeploymentModelAnalysisRequest(
        embeddedDeploymentModelAnalysisRequest);
  }

  /**
   * Handle an AnalysisTaskResponse. Update the task based on the sucess flag. Then determine the
   * next task to run. If the former task used static analysis technique, create a new dynamic task,
   * search for a plugin and send an AnalysisTaskStartRequest. Otherwise, search for a waiting
   * subTask of the completed analysis task. If all this failes, look if other tasks wait for
   * execution.
   *
   * @param analysisTaskResponse
   */
  public void handleAnalysisTaskResponse(AnalysisTaskResponse analysisTaskResponse) {
    AnalysisTask analysisTask =
        analysisTaskService.updateStatusFromAnalysisTaskResponse(analysisTaskResponse);
    Plugin plugin;
    if (analysisTask.getAnalysisType() == AnalysisType.STATIC) {
      try {
        plugin =
            pluginService.getPluginByTechnologyAndAnalysisType(
                analysisTask.getTechnology(), AnalysisType.DYNAMIC);
        AnalysisTask newTask =
            analysisTaskService.createDynamicTaskFromStaticTask(analysisTask, plugin.getId());
        sendAnalysisTask(newTask);
        return;
      } catch (PluginException e) {
        LOG.error(e.getMessage());
      }
    }
    Optional<AnalysisTask> subTaskToRunOpt =
        analysisTask.getSubTasks().stream()
            .filter(task -> task.getStatus() == AnalysisStatus.WAITING)
            .findFirst();
    if (subTaskToRunOpt.isPresent()) {
      AnalysisTask subTaskToRun = subTaskToRunOpt.get();
      try {
        plugin = pluginService.getPluginByTechnology(subTaskToRun.getTechnology());
        AnalysisTask updatedTask =
            analysisTaskService.updateStatusToRunning(
                subTaskToRun, plugin.getId(), plugin.getAnalysisType());
        sendAnalysisTask(updatedTask);
        return;
      } catch (PluginException pluginException) {
        LOG.error(pluginException.getMessage());
        analysisTaskService.updateStatusToFailed(subTaskToRun);
      }
    }
    runNextWaitingTask(analysisTask.getTransformationProcessId());
  }

  /**
   * Run the next AnalysisTask with status == AnalysisStatus.WAITING. Else, search for a plugin. If
   * no plugin can be found, try the next waiting task. If a plugin can be found update the analysis
   * task and send a request.
   *
   * @param transformationProcessId
   */
  private void runNextWaitingTask(UUID transformationProcessId) {
    while (Boolean.TRUE.equals(analysisTaskService.areTasksWaiting(transformationProcessId))) {
      AnalysisTask analysisTask =
          analysisTaskService.getOneWaitingAnalysisTask(transformationProcessId);
      Plugin plugin;
      try {
        plugin = pluginService.getPluginByTechnology(analysisTask.getTechnology());
        AnalysisTask updatedTask =
            analysisTaskService.updateStatusToRunning(
                analysisTask, plugin.getId(), plugin.getAnalysisType());
        sendAnalysisTask(updatedTask);
        return;
      } catch (PluginException pluginException) {
        LOG.error(pluginException.getMessage());
        analysisTaskService.updateStatusToFailed(analysisTask);
      }
    }
  }

  /**
   * Send a AnalysisTaskStartRequest based on a given AnalysisTask. If an error occures, set the
   * task to failed and look if other tasks wait for execution.
   *
   * @param analysisTask
   */
  private void sendAnalysisTask(AnalysisTask analysisTask) {
    try {
      analysisTaskSender.send(analysisTask);
    } catch (JsonProcessingException | AmqpException e) {
      e.printStackTrace();
      analysisTaskService.updateStatusToFailed(analysisTask);
      runNextWaitingTask(analysisTask.getTransformationProcessId());
    }
  }
}

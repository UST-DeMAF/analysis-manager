package ust.tad.analysismanager.analysistask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ust.tad.analysismanager.analysistaskresponse.AnalysisTaskResponse;
import ust.tad.analysismanager.analysistaskresponse.EmbeddedDeploymentModelAnalysisRequest;
import ust.tad.analysismanager.plugin.PluginRepository;
import ust.tad.analysismanager.shared.AnalysisType;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class AnalysisTaskService {

  private static final Logger LOG = LoggerFactory.getLogger(AnalysisTaskService.class);

  private final HashMap<UUID, Process> processes = new HashMap<>();

  @Autowired private AnalysisTaskRepository analysisTaskRepository;

  @Autowired private LocationService locationService;

  @Autowired private TADMEntitiesService tadmEntitiesService;

  @Autowired private PluginRepository pluginRepository;

  public AnalysisTask createAnalysisTask(
      UUID transformationProcessId,
      String technology,
      AnalysisType analysisType,
      URL url,
      List<String> commands,
      List<String> options,
      UUID pluginId) {

    Location location = locationService.createLocation(url);
    List<Location> locations = new ArrayList<>();
    locations.add(location);

    AnalysisTask newAnalysisTask = new AnalysisTask();
    newAnalysisTask.setTransformationProcessId(transformationProcessId);
    newAnalysisTask.setTechnology(technology);
    newAnalysisTask.setAnalysisType(analysisType);
    newAnalysisTask.setCommands(commands);
    newAnalysisTask.setOptions(options);
    newAnalysisTask.setPluginId(pluginId);
    newAnalysisTask.setLocations(locations);

    AnalysisTask analysisTask = analysisTaskRepository.save(newAnalysisTask);

    if (processes.isEmpty() || !processes.containsKey(transformationProcessId)) {
      Process process = new Process();
      process.mainTaskId = analysisTask.getTaskId();
      process.transformationProcessId = transformationProcessId;
      process.options = options;
      process.activeTasks++;

      if (!technology.equals("visualization-service")) {
        boolean visualize = true;
        for (String option : options) {
          if (option.contains("visualize=false")) {
            visualize = false;
            break;
          }
        }

        if (visualize && pluginRepository.findByTechnology("visualization-service").isEmpty()) {
          LOG.error(
              "Visualization service is not registered, transformation continues without visualization.");
          visualize = false;
        }

        process.visualize = visualize;
      }

      processes.put(transformationProcessId, process);
    } else {
      Process process = processes.get(transformationProcessId);
      process.activeTasks++;
    }
    return analysisTask;
  }

  public AnalysisTask createAnalysisTaskFromEmbeddedDeploymentModelAnalysisRequest(
      EmbeddedDeploymentModelAnalysisRequest request) {
    List<Location> savedLocations = locationService.saveLocations(request.getLocations());

    AnalysisTask analysisTask = new AnalysisTask();
    analysisTask.setTransformationProcessId(request.getTransformationProcessId());
    analysisTask.setTechnology(request.getTechnology());
    analysisTask.setCommands(request.getCommands());
    analysisTask.setOptions(request.getOptions());
    analysisTask.setLocations(savedLocations);
    analysisTask.setTadmEntities(tadmEntitiesService.createAndSaveAllTADMEntities(
            request.getTadmEntities()));
    analysisTask.setStatus(AnalysisStatus.WAITING);
    AnalysisTask newTask = analysisTaskRepository.save(analysisTask);

    addSubtask(request.getParentTaskId(), newTask);
    return newTask;
  }

  public AnalysisTask createDynamicTaskFromStaticTask(AnalysisTask staticTask, UUID pluginId) {
    List<Location> locations = locationService.duplicateLocations(staticTask.getLocations());

    AnalysisTask newAnalysisTask = new AnalysisTask();
    newAnalysisTask.setTransformationProcessId(staticTask.getTransformationProcessId());
    newAnalysisTask.setTechnology(staticTask.getTechnology());
    newAnalysisTask.setAnalysisType(AnalysisType.DYNAMIC);
    newAnalysisTask.setCommands(staticTask.getCommands());
    newAnalysisTask.setOptions(staticTask.getOptions());
    newAnalysisTask.setPluginId(pluginId);
    newAnalysisTask.setLocations(locations);
    newAnalysisTask.setTadmEntities(staticTask.getTadmEntities());
    return analysisTaskRepository.save(newAnalysisTask);
  }

  public AnalysisTask addSubtask(UUID parentTaskId, AnalysisTask subTask) {
    AnalysisTask parentTask = analysisTaskRepository.getByTaskId(parentTaskId);
    parentTask.addSubtask(subTask);

    Process process = processes.get(parentTask.getTransformationProcessId());
    process.activeTasks++;

    return analysisTaskRepository.save(parentTask);
  }

  public AnalysisTask updateStatusFromAnalysisTaskResponse(AnalysisTaskResponse response) {
    boolean responseSuccess = response.getSuccess();
    UUID responseTaskId = response.getTaskId();

    AnalysisTask task = analysisTaskRepository.getByTaskId(responseTaskId);

    if (responseSuccess) {
      task.setStatus(AnalysisStatus.FINISHED);
    } else {
      task.setStatus(AnalysisStatus.FAILED);
    }

    Process process = processes.get(task.getTransformationProcessId());
    process.activeTasks--;

    if (process.visualize
        && process.activeTasks == 0
        && process.visualizationTaskId == null
        && responseSuccess) {
      AnalysisTask newVisualizationTask = new AnalysisTask();

      newVisualizationTask.setTransformationProcessId(process.transformationProcessId);
      newVisualizationTask.setTechnology("visualization-service");
      newVisualizationTask.setOptions(process.options);
      newVisualizationTask.setStatus(AnalysisStatus.WAITING);

      AnalysisTask visualizationTask = analysisTaskRepository.save(newVisualizationTask);
      addSubtask(process.mainTaskId, visualizationTask);
      process.visualizationTaskId = visualizationTask.getTaskId();
    }
    return analysisTaskRepository.save(task);
  }

  public AnalysisTask updateStatusToFailed(AnalysisTask analysisTask) {
    analysisTask.setStatus(AnalysisStatus.FAILED);
    return analysisTaskRepository.save(analysisTask);
  }

  public AnalysisTask updateStatusToRunning(
      AnalysisTask analysisTask, UUID pluginId, AnalysisType analysisType) {
    analysisTask.setAnalysisType(analysisType);
    analysisTask.setPluginId(pluginId);
    analysisTask.setStatus(AnalysisStatus.RUNNING);
    return analysisTaskRepository.save(analysisTask);
  }

  public AnalysisTask getTaskByTaskId(UUID taskId) {
    return analysisTaskRepository.getByTaskId(taskId);
  }

  public AnalysisTask getOneWaitingAnalysisTask(UUID transformationProcessId) {
    return analysisTaskRepository
        .getByStatusAndTransformationProcessId(AnalysisStatus.WAITING, transformationProcessId)
        .get(0);
  }

  public Boolean areTasksWaiting(UUID transformationProcessId) {
    return !analysisTaskRepository
        .getByStatusAndTransformationProcessId(AnalysisStatus.WAITING, transformationProcessId)
        .isEmpty();
  }

  public Boolean areTasksWaitingOrRunning(UUID transformationProcessId)
      throws TransformationProcessNotExistingException {
    if (!analysisTaskRepository.existsByTransformationProcessId(transformationProcessId)) {
      throw new TransformationProcessNotExistingException(
          "A transformation process with this id does not exist.");
    }

    return !analysisTaskRepository
            .getByStatusAndTransformationProcessId(AnalysisStatus.WAITING, transformationProcessId)
            .isEmpty()
        || !analysisTaskRepository
            .getByStatusAndTransformationProcessId(AnalysisStatus.RUNNING, transformationProcessId)
            .isEmpty();
  }

  public Boolean hasNoTaskFailed(UUID transformationProcessId) {
    return analysisTaskRepository
        .getByStatusAndTransformationProcessId(AnalysisStatus.FAILED, transformationProcessId)
        .isEmpty();
  }

  /**
   * Returns the analysis process with the given transformation process id.
   *
   * @param transformationProcessId
   * @return the analysis process
   */
  public Process getProcess(UUID transformationProcessId) {
    return processes.get(transformationProcessId);
  }

  /**
   * Removes an analysis process from the list of analysis processes.
   *
   * @param transformationProcessId
   */
  public void removeProcess(UUID transformationProcessId) {
    processes.remove(transformationProcessId);
  }

  /**
   * A process that contains the main task id, the transformation process id, the visualization task
   * id, a boolean indicating if the process should be visualized, and the number of active tasks.
   */
  public static class Process {
    public UUID mainTaskId = null;
    public UUID transformationProcessId = null;
    public UUID visualizationTaskId = null;
    public boolean visualize = false;
    public int activeTasks = 0;
    public List<String> options;
  }
}

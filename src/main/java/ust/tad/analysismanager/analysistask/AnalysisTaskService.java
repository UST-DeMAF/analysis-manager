package ust.tad.analysismanager.analysistask;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ust.tad.analysismanager.analysistaskresponse.AnalysisTaskResponse;
import ust.tad.analysismanager.analysistaskresponse.EmbeddedDeploymentModelAnalysisRequest;
import ust.tad.analysismanager.shared.AnalysisType;

@Service
public class AnalysisTaskService {

    @Autowired
    private AnalysisTaskRepository analysisTaskRepository;

    @Autowired
    private LocationService locationService;

    
    public AnalysisTask createAnalysisTask(
        UUID transformationProcessId, 
        String technology, 
        AnalysisType analysisType, 
        URL url, 
        List<String> commands, 
        UUID pluginId) {

        Location location = locationService.createLocation(url);
        List<Location> locations = new ArrayList<>();
        locations.add(location);

        AnalysisTask newAnalysisTask = new AnalysisTask();
        newAnalysisTask.setTransformationProcessId(transformationProcessId);
        newAnalysisTask.setTechnology(technology);
        newAnalysisTask.setAnalysisType(analysisType);
        newAnalysisTask.setCommands(commands);
        newAnalysisTask.setPluginId(pluginId);        
        newAnalysisTask.setLocations(locations);
        return analysisTaskRepository.save(newAnalysisTask);        
    }

    public AnalysisTask createAnalysisTaskFromEmbeddedDeploymentModelAnalysisRequest(EmbeddedDeploymentModelAnalysisRequest request) {
        List<Location> savedLocations = locationService.saveLocations(request.getLocations());

        AnalysisTask analysisTask = new AnalysisTask();
        analysisTask.setTransformationProcessId(request.getTransformationProcessId());
        analysisTask.setTechnology(request.getTechnology());
        analysisTask.setCommands(request.getCommands());
        analysisTask.setLocations(savedLocations);
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
        newAnalysisTask.setPluginId(pluginId);        
        newAnalysisTask.setLocations(locations);
        return analysisTaskRepository.save(newAnalysisTask);    
    }

    public AnalysisTask addSubtask(UUID parentTaskId, AnalysisTask subTask) {
        AnalysisTask parentTask = analysisTaskRepository.getByTaskId(parentTaskId);
        parentTask.addSubtask(subTask);
        return analysisTaskRepository.save(parentTask);        
    }

    public AnalysisTask updateStatusFromAnalysisTaskResponse(AnalysisTaskResponse response) {
        AnalysisTask task = analysisTaskRepository.getByTaskId(response.getTaskId());
        if(response.getSuccess()) {
            task.setStatus(AnalysisStatus.FINISHED);
        } else {
            task.setStatus(AnalysisStatus.FAILED);            
        }
        return analysisTaskRepository.save(task);
    }

    public AnalysisTask updateStatusToFailed(AnalysisTask analysisTask) {
        analysisTask.setStatus(AnalysisStatus.FAILED);
        return analysisTaskRepository.save(analysisTask);
    }

    public AnalysisTask updateStatusToRunning(AnalysisTask analysisTask, UUID pluginId, AnalysisType analysisType) {
        analysisTask.setAnalysisType(analysisType);
        analysisTask.setPluginId(pluginId);
        analysisTask.setStatus(AnalysisStatus.RUNNING);
        return analysisTaskRepository.save(analysisTask);
    }

    public AnalysisTask getTaskByTaskId(UUID taskId) {
        return analysisTaskRepository.getByTaskId(taskId);
    }

    public AnalysisTask getOneWaitingAnalysisTask(UUID transformationProcessId) {
        return analysisTaskRepository.getByStatusAndTransformationProcessId(AnalysisStatus.WAITING, transformationProcessId).get(0);
    }

    public Boolean areTasksWaiting(UUID transformationProcessId) {
        return !analysisTaskRepository.getByStatusAndTransformationProcessId(AnalysisStatus.WAITING, transformationProcessId).isEmpty();
    }

    public Boolean areTasksWaitingOrRunning(UUID transformationProcessId) throws TransformationProcessNotExistingException {
        if(!analysisTaskRepository.existsByTransformationProcessId(transformationProcessId)) {
            throw new TransformationProcessNotExistingException("A transformation process with this id does not exist.");
        }

        return !analysisTaskRepository.getByStatusAndTransformationProcessId(AnalysisStatus.WAITING, transformationProcessId).isEmpty() || 
        !analysisTaskRepository.getByStatusAndTransformationProcessId(AnalysisStatus.RUNNING, transformationProcessId).isEmpty();
    }


}

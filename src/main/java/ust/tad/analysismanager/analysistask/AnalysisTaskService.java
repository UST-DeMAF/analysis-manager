package ust.tad.analysismanager.analysistask;

import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        AnalysisTask newAnalysisTask = new AnalysisTask();
        newAnalysisTask.setTransformationProcessId(transformationProcessId);
        newAnalysisTask.setTechnology(technology);
        newAnalysisTask.setAnalysisType(analysisType);
        newAnalysisTask.setCommands(commands);
        newAnalysisTask.setPluginId(pluginId);        
        newAnalysisTask.setLocations(List.of(location));
        return analysisTaskRepository.save(newAnalysisTask);        
    }

}

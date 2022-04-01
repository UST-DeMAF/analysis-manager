package ust.tad.analysismanager.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ust.tad.analysismanager.shared.AnalysisType;


@Service
public class PluginService {

    @Autowired
    PluginRepository pluginRepository;

    public List<Plugin> getAllPlugins() {
        return pluginRepository.findAll();
    }

    public String getQueueNameForPlugin(String technology, AnalysisType analysisType) {
        return pluginRepository.findByTechnologyAndAnalysisType(technology, analysisType).get(0).getQueueName();        
    }
    
}

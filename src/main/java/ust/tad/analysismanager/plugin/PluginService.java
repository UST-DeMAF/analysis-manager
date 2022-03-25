package ust.tad.analysismanager.plugin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PluginService {

    @Autowired
    PluginRepository pluginRepository;

    public List<Plugin> getAllPlugins() {
        return pluginRepository.findAll();
    }
    
}

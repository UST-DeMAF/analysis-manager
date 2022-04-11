package ust.tad.analysismanager.plugin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ust.tad.analysismanager.shared.AnalysisType;


@Service
public class PluginService {

    @Autowired
    PluginRepository pluginRepository;

    /**
     * Find all registered Plugins.
     * 
     * @return a List of all Plugins.
     */
    public List<Plugin> getAllPlugins() {
        return pluginRepository.findAll();
    }

    /**
     * Finds a Plugin, identified by the technology and analysisType.
     * 
     * @param technology
     * @param analysisType
     * @return the Plugin.
     * @throws PluginException if no appropriate plugin can be found.
     */
    public Plugin getPluginByTechnologyAndAnalysisType(String technology, AnalysisType analysisType) throws PluginException {
        try {
            return pluginRepository.findByTechnologyAndAnalysisType(technology, analysisType).get(0);  
        } catch (IndexOutOfBoundsException e) {
            throw new PluginException(
                String.format("No appropriate plugin currently registered for technology: '%s' and anlalysisType: '%s'.", 
                technology, analysisType.toString()));
        }    
    }

    /**
     * Gets a Plugin for the given deployment technology.
     * First looks for a static plugin.
     * If there is no static plugin, it looks for a dynamic plugin.
     * When no plugin can be found a PluginException is thrown.
     * 
     * @param technology
     * @return the found plugin.
     * @throws PluginException
     */
    public Plugin getPluginByTechnology(String technology) throws PluginException {
        List<Plugin> plugins = pluginRepository.findByTechnology(technology);
        Optional<Plugin> plugin = plugins.stream().filter(p -> p.getAnalysisType() == AnalysisType.STATIC).findAny();
        if (plugin.isPresent()) {
            return plugin.get();
        } else {
            plugin = plugins.stream().filter(p -> p.getAnalysisType() == AnalysisType.DYNAMIC).findAny();
            return plugin.orElseThrow(() -> new PluginException(
                String.format("No appropriate static or dynamic plugin currently registered for technology: '%s'.", technology)));
        }
    }
    
}

package ust.tad.analysismanager.shell;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ust.tad.analysismanager.plugin.Plugin;
import ust.tad.analysismanager.plugin.PluginService;

@ShellComponent
@ShellCommandGroup("Plugin Commands")
public class ListPluginsCommand {

    @Autowired
    PluginService pluginService;

    @ShellMethod("List all registered plugins.")
    public String plugins() {
        List<Plugin> plugins = pluginService.getAllPlugins();
        return plugins.toString();
    }
    
}

package ust.tad.analysismanager.shell;

import java.util.List;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
    @Autowired
    private Terminal terminal;

    @ShellMethod("List all registered plugins.")
    public void plugins() {
        List<Plugin> plugins = pluginService.getAllPlugins();

        terminal.writer().println();
        terminal.writer().println("The following plugins are currently registered:");
        for (Plugin plugin : plugins) {
            terminal.writer().println(plugin.getTechnology());
        }
        terminal.flush();
    }
    
}

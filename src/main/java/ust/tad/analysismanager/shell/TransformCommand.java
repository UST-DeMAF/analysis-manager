package ust.tad.analysismanager.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("Transformation Commands")
public class TransformCommand {
    @ShellMethod("Transform technology-specific deployment model to technology-agnostic deployment model.")
    public String transform(String technology, String location, String command) {
        return String.format("Transform %s deployment model at %s using commands %s", technology, location, command);
    }
}

package ust.tad.analysismanager.shell;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ust.tad.analysismanager.deploymenttechnology.DeploymentTechnology;
import ust.tad.analysismanager.deploymenttechnology.DeploymentTechnologyService;

@ShellComponent
@ShellCommandGroup("Deployment Technology Commands")
public class AddTechnologyCommand {

    @Autowired
    DeploymentTechnologyService deploymentTechnologyService;

    @ShellMethod("Add a deployment technology to the list of supported deployment technologies.")
    public String add(String technology) {
       DeploymentTechnology addedTechnology = deploymentTechnologyService.save(new DeploymentTechnology(technology));
       return String.format("Technology %s added", addedTechnology.getName());
    }

    @ShellMethod("List the supported deployment technologies.")
    public String sup() {
       List<DeploymentTechnology> technologies = deploymentTechnologyService.findAll();
       return String.format("The following deployment technologies are currently supported by the tranformation framwork: %s", 
        technologies.stream().map(technology -> technology.getName()).collect(Collectors.joining(", ")));
    }
}

package ust.tad.analysismanager.shell;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import ust.tad.analysismanager.analysistask.AnalysisTask;
import ust.tad.analysismanager.analysistask.AnalysisTaskSender;
import ust.tad.analysismanager.analysistask.AnalysisType;
import ust.tad.analysismanager.deploymenttechnology.DeploymentTechnology;

@ShellComponent
@ShellCommandGroup("Transformation Commands")
public class TransformCommand {

    @Autowired
    AnalysisTaskSender analysisTaskSender;

    @ShellMethod("Transform technology-specific deployment model to technology-agnostic deployment model.")
    public String transform(String technology, String location, String command) throws JsonProcessingException {
        AnalysisTask analysisTask = new AnalysisTask(new DeploymentTechnology(technology), AnalysisType.STATIC, location, List.of(command));
        try {
            analysisTaskSender.send(analysisTask);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AmqpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String key = analysisTask.getTechnology().getName().toString() + "." + analysisTask.getAnalysisType().toString();
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(analysisTask);
        return String.format("Key: %s   Message: %s", key, message);
        //return String.format("Transform %s deployment model at %s using commands %s", technology, location, command);
    }
}

package ust.tad.analysismanager.analysistask;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.Id;

import ust.tad.analysismanager.deploymenttechnology.DeploymentTechnology;

public class AnalysisTask {

    @Id
    private final UUID taskId = UUID.randomUUID();
    
    private DeploymentTechnology technology;

    private AnalysisType analysisType;

    private String location;

    private List<String> commands;

    public AnalysisTask() {
    }

    public AnalysisTask(DeploymentTechnology technology, AnalysisType analysisType, String location, List<String> commands) {
        this.technology = technology;
        this.analysisType = analysisType;
        this.location = location;
        this.commands = commands;
    }

    public UUID getTaskId() {
        return this.taskId;
    }

    public DeploymentTechnology getTechnology() {
        return this.technology;
    }

    public void setTechnology(DeploymentTechnology technology) {
        this.technology = technology;
    }

    public AnalysisType getAnalysisType() {
        return this.analysisType;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public AnalysisTask technology(DeploymentTechnology technology) {
        setTechnology(technology);
        return this;
    }

    public AnalysisTask analysisType(AnalysisType analysisType) {
        setAnalysisType(analysisType);
        return this;
    }

    public AnalysisTask location(String location) {
        setLocation(location);
        return this;
    }

    public AnalysisTask commands(List<String> commands) {
        setCommands(commands);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AnalysisTask)) {
            return false;
        }
        AnalysisTask analysisTask = (AnalysisTask) o;
        return Objects.equals(taskId, analysisTask.taskId) && Objects.equals(technology, analysisTask.technology) && Objects.equals(analysisType, analysisTask.analysisType) && Objects.equals(location, analysisTask.location) && Objects.equals(commands, analysisTask.commands);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, technology, analysisType, location, commands);
    }

    @Override
    public String toString() {
        return "{" +
            " taskId='" + getTaskId() + "'" +
            ", technology='" + getTechnology() + "'" +
            ", analysisType='" + getAnalysisType() + "'" +
            ", location='" + getLocation() + "'" +
            ", commands='" + getCommands() + "'" +
            "}";
    }
    

}

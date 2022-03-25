package ust.tad.analysismanager.analysistask;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import ust.tad.analysismanager.shared.AnalysisType;

@Entity
public class AnalysisTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID taskId;

    private UUID transformationProcessId;
    
    private String technology;

    private AnalysisType analysisType;

    @ElementCollection
    private List<String> commands;

    private AnalysisStatus status = AnalysisStatus.RUNNING;

    private String pluginQueueName;
    
    @OneToMany
    //@JoinColumn(name = "location_id", referencedColumnName = "id")
    private List<Location> locations;

    @OneToMany
    @JoinColumn(name = "task_id", referencedColumnName = "taskId")
    private List<AnalysisTask> subTasks;


    public AnalysisTask() {
    }

    public AnalysisTask(UUID transformationProcessId, String technology, AnalysisType analysisType, List<String> commands, AnalysisStatus status, String pluginQueueName, List<Location> locations, List<AnalysisTask> subTasks) {
        this.transformationProcessId = transformationProcessId;
        this.technology = technology;
        this.analysisType = analysisType;
        this.commands = commands;
        this.status = status;
        this.pluginQueueName = pluginQueueName;
        this.locations = locations;
        this.subTasks = subTasks;
    }

    public UUID getTaskId() {
        return this.taskId;
    }

    public UUID getTransformationProcessId() {
        return this.transformationProcessId;
    }

    public void setTransformationProcessId(UUID transformationProcessId) {
        this.transformationProcessId = transformationProcessId;
    }

    public String getTechnology() {
        return this.technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public AnalysisType getAnalysisType() {
        return this.analysisType;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public AnalysisStatus getStatus() {
        return this.status;
    }

    public void setStatus(AnalysisStatus status) {
        this.status = status;
    }

    public String getPluginQueueName() {
        return this.pluginQueueName;
    }

    public void setPluginQueueName(String pluginQueueName) {
        this.pluginQueueName = pluginQueueName;
    }

    public List<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public List<AnalysisTask> getSubTasks() {
        return this.subTasks;
    }

    public void setSubTasks(List<AnalysisTask> subTasks) {
        this.subTasks = subTasks;
    }

    public AnalysisTask transformationProcessId(UUID transformationProcessId) {
        setTransformationProcessId(transformationProcessId);
        return this;
    }

    public AnalysisTask technology(String technology) {
        setTechnology(technology);
        return this;
    }

    public AnalysisTask analysisType(AnalysisType analysisType) {
        setAnalysisType(analysisType);
        return this;
    }

    public AnalysisTask commands(List<String> commands) {
        setCommands(commands);
        return this;
    }

    public AnalysisTask status(AnalysisStatus status) {
        setStatus(status);
        return this;
    }

    public AnalysisTask pluginQueueName(String pluginQueueName) {
        setPluginQueueName(pluginQueueName);
        return this;
    }

    public AnalysisTask locations(List<Location> locations) {
        setLocations(locations);
        return this;
    }

    public AnalysisTask subTasks(List<AnalysisTask> subTasks) {
        setSubTasks(subTasks);
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
        return Objects.equals(taskId, analysisTask.taskId) && Objects.equals(transformationProcessId, analysisTask.transformationProcessId) && Objects.equals(technology, analysisTask.technology) && Objects.equals(analysisType, analysisTask.analysisType) && Objects.equals(commands, analysisTask.commands) && Objects.equals(status, analysisTask.status) && Objects.equals(pluginQueueName, analysisTask.pluginQueueName) && Objects.equals(locations, analysisTask.locations) && Objects.equals(subTasks, analysisTask.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, transformationProcessId, technology, analysisType, commands, status, pluginQueueName, locations, subTasks);
    }

    @Override
    public String toString() {
        return "{" +
            " taskId='" + getTaskId() + "'" +
            ", transformationProcessId='" + getTransformationProcessId() + "'" +
            ", technology='" + getTechnology() + "'" +
            ", analysisType='" + getAnalysisType() + "'" +
            ", commands='" + getCommands() + "'" +
            ", status='" + getStatus() + "'" +
            ", pluginQueueName='" + getPluginQueueName() + "'" +
            ", locations='" + getLocations() + "'" +
            ", subTasks='" + getSubTasks() + "'" +
            "}";
    }

}
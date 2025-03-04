package ust.tad.analysismanager.analysistask;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ust.tad.analysismanager.shared.AnalysisType;

@Entity
@Table(name = "AnalysisTask")
public class AnalysisTask {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID taskId;

  private UUID transformationProcessId;

  private String technology;

  private AnalysisType analysisType;

  @ElementCollection(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<String> commands = new ArrayList<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<String> options = new ArrayList<>();

  private AnalysisStatus status = AnalysisStatus.RUNNING;

  private UUID pluginId;

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<Location> locations = new ArrayList<>();

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "parentTaskId", referencedColumnName = "taskId")
  @Fetch(value = FetchMode.SUBSELECT)
  private List<AnalysisTask> subTasks = new ArrayList<>();

  @OneToMany(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<TADMEntities> tadmEntities = new ArrayList<>();

  public AnalysisTask() {}

  public AnalysisTask(
      UUID transformationProcessId,
      String technology,
      AnalysisType analysisType,
      List<String> commands,
      List<String> options,
      AnalysisStatus status,
      UUID pluginId,
      List<Location> locations,
      List<AnalysisTask> subTasks,
      List<TADMEntities> tadmEntities) {
    this.transformationProcessId = transformationProcessId;
    this.technology = technology;
    this.analysisType = analysisType;
    this.commands = commands;
    this.options = options;
    this.status = status;
    this.pluginId = pluginId;
    this.locations = locations;
    this.subTasks = subTasks;
    this.tadmEntities = tadmEntities;
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

  public List<String> getOptions() {
    return this.options;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public void setStatus(AnalysisStatus status) {
    this.status = status;
  }

  public UUID getPluginId() {
    return this.pluginId;
  }

  public void setPluginId(UUID pluginId) {
    this.pluginId = pluginId;
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

  public List<TADMEntities> getTadmEntities() {
    return tadmEntities;
  }

  public void setTadmEntities(List<TADMEntities> tadmEntities) {
    this.tadmEntities = tadmEntities;
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

  public AnalysisTask options(List<String> options) {
    setOptions(options);
    return this;
  }

  public AnalysisTask status(AnalysisStatus status) {
    setStatus(status);
    return this;
  }

  public AnalysisTask pluginId(UUID pluginId) {
    setPluginId(pluginId);
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

  public AnalysisTask tadmEntities(List<TADMEntities> tadmEntities) {
    setTadmEntities(tadmEntities);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof AnalysisTask)) {
      return false;
    }
    AnalysisTask analysisTask = (AnalysisTask) o;
    return Objects.equals(taskId, analysisTask.taskId)
        && Objects.equals(transformationProcessId, analysisTask.transformationProcessId)
        && Objects.equals(technology, analysisTask.technology)
        && Objects.equals(analysisType, analysisTask.analysisType)
        && Objects.equals(commands, analysisTask.commands)
        && Objects.equals(options, analysisTask.options)
        && Objects.equals(status, analysisTask.status)
        && Objects.equals(pluginId, analysisTask.pluginId)
        && Objects.equals(locations, analysisTask.locations)
        && Objects.equals(subTasks, analysisTask.subTasks)
        && Objects.equals(tadmEntities, analysisTask.tadmEntities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        taskId,
        transformationProcessId,
        technology,
        analysisType,
        commands,
        options,
        status,
        pluginId,
        locations,
        subTasks,
        tadmEntities);
  }

  @Override
  public String toString() {
    return "{"
        + " taskId='"
        + getTaskId()
        + "'"
        + ", transformationProcessId='"
        + getTransformationProcessId()
        + "'"
        + ", technology='"
        + getTechnology()
        + "'"
        + ", analysisType='"
        + getAnalysisType()
        + "'"
        + ", commands='"
        + getCommands()
        + "'"
        + ", options='"
        + getOptions()
        + "'"
        + ", status='"
        + getStatus()
        + "'"
        + ", pluginQueueName='"
        + getPluginId()
        + "'"
        + ", locations='"
        + getLocations()
        + "'"
        + ", subTasks='"
        + getSubTasks()
        + "'"
        + ", tadmEntities='"
        + getTadmEntities()
        + "'"
        + "}";
  }

  public void addSubtask(AnalysisTask subTask) {
    this.subTasks.add(subTask);
  }
}

package ust.tad.analysismanager.transformationprocess;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import ust.tad.analysismanager.analysistask.Location;

public class InitializeTechnologySpecificDeploymentModelRequest {

  private UUID transformationProcessId;

  private String technology;

  private List<String> commands;

  private List<String> options;

  private List<Location> locations;

  public InitializeTechnologySpecificDeploymentModelRequest() {}

  public InitializeTechnologySpecificDeploymentModelRequest(
      UUID transformationProcessId,
      String technology,
      List<String> commands,
      List<String> options,
      List<Location> locations) {
    this.transformationProcessId = transformationProcessId;
    this.technology = technology;
    this.commands = commands;
    this.options = options;
    this.locations = locations;
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

  public List<String> getCommands() {
    return this.commands;
  }

  public void setCommands(List<String> commands) {
    this.commands = commands;
  }

  public List<String> getOptions() {
    return this.options;
  }

  public void setOptions(List<String> options) {
    this.options = options;
  }

  public List<Location> getLocations() {
    return this.locations;
  }

  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

  public InitializeTechnologySpecificDeploymentModelRequest transformationProcessId(
      UUID transformationProcessId) {
    setTransformationProcessId(transformationProcessId);
    return this;
  }

  public InitializeTechnologySpecificDeploymentModelRequest technology(String technology) {
    setTechnology(technology);
    return this;
  }

  public InitializeTechnologySpecificDeploymentModelRequest commands(List<String> commands) {
    setCommands(commands);
    return this;
  }

  public InitializeTechnologySpecificDeploymentModelRequest options(List<String> options) {
    setOptions(options);
    return this;
  }

  public InitializeTechnologySpecificDeploymentModelRequest locations(List<Location> locations) {
    setLocations(locations);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof InitializeTechnologySpecificDeploymentModelRequest)) {
      return false;
    }
    InitializeTechnologySpecificDeploymentModelRequest
        initializeTechnologySpecificDeploymentModelRequest =
            (InitializeTechnologySpecificDeploymentModelRequest) o;
    return Objects.equals(
            transformationProcessId,
            initializeTechnologySpecificDeploymentModelRequest.transformationProcessId)
        && Objects.equals(technology, initializeTechnologySpecificDeploymentModelRequest.technology)
        && Objects.equals(commands, initializeTechnologySpecificDeploymentModelRequest.commands)
        && Objects.equals(options, initializeTechnologySpecificDeploymentModelRequest.options)
        && Objects.equals(locations, initializeTechnologySpecificDeploymentModelRequest.locations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transformationProcessId, technology, commands, options, locations);
  }

  @Override
  public String toString() {
    return "{"
        + " transformationProcessId='"
        + getTransformationProcessId()
        + "'"
        + ", technology='"
        + getTechnology()
        + "'"
        + ", commands='"
        + getCommands()
        + "'"
        + ", options='"
        + getOptions()
        + "'"
        + ", locations='"
        + getLocations()
        + "'"
        + "}";
  }
}

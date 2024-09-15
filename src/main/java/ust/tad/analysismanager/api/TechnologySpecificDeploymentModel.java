package ust.tad.analysismanager.api;

import java.net.URL;
import java.util.List;

/** A technology-specific deployment model. */
public class TechnologySpecificDeploymentModel {

  /** The deployment technology used to create the technology-specific deployment model. */
  private String technology;

  /** A URL to the location of the technology-specific deployment model. */
  private URL locationURL;

  /** A list of commands used for executing the technology-specific deployment model. */
  private List<String> commands;

  /*
   * A list of options used for influence the visualization of the technology-specific deployment model.
   */
  private List<String> options;

  public TechnologySpecificDeploymentModel() {}

  public TechnologySpecificDeploymentModel(
      String technology, URL locationURL, List<String> commands, List<String> options) {
    this.technology = technology;
    this.locationURL = locationURL;
    this.commands = commands;
    this.options = options;
  }

  public String getTechnology() {
    return this.technology;
  }

  public void setTechnology(String technology) {
    this.technology = technology;
  }

  public URL getLocationURL() {
    return this.locationURL;
  }

  public void setLocationURL(URL locationURL) {
    this.locationURL = locationURL;
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

  @Override
  public String toString() {
    return "{"
        + " technology='"
        + getTechnology()
        + "'"
        + ", locationURL='"
        + getLocationURL()
        + "'"
        + ", commands='"
        + getCommands()
        + "'"
        + ", options='"
        + getOptions()
        + "'"
        + "}";
  }
}

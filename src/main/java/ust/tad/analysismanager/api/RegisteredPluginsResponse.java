package ust.tad.analysismanager.api;

import java.util.ArrayList;
import java.util.List;

/** A response message containing a list of the currently registered plugins. */
public class RegisteredPluginsResponse {

  /** A list with the names of the registered plugins. */
  private List<String> pluginNames = new ArrayList<>();

  public RegisteredPluginsResponse() {}

  public RegisteredPluginsResponse(List<String> pluginNames) {
    this.pluginNames = pluginNames;
  }

  public List<String> getPluginNames() {
    return this.pluginNames;
  }

  public void setPluginNames(List<String> pluginNames) {
    this.pluginNames = pluginNames;
  }

  @Override
  public String toString() {
    return "{" + " pluginNames='" + getPluginNames() + "'" + "}";
  }
}

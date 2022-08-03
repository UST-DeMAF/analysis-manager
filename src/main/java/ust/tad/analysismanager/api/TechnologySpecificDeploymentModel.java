package ust.tad.analysismanager.api;

import java.net.URL;
import java.util.List;

/**
 * A technology-specific deployment model.
 */
public class TechnologySpecificDeploymentModel {

    /**
     * The deployment technology used to create the technology-specific deployment model.
     */
    private String technology;

    /**
     * A URL to the location of the technology-specific deployment model.
     */
    private URL locationURL;

    /**
     * A list of commands used for executing the technology-specific deployment model.
     */
    private List<String> commands;


    public TechnologySpecificDeploymentModel() {
    }

    public TechnologySpecificDeploymentModel(String technology, URL locationURL, List<String> commands) {
        this.technology = technology;
        this.locationURL = locationURL;
        this.commands = commands;
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

    @Override
    public String toString() {
        return "{" +
            " technology='" + getTechnology() + "'" +
            ", locationURL='" + getLocationURL() + "'" +
            ", commands='" + getCommands() + "'" +
            "}";
    }
    
}

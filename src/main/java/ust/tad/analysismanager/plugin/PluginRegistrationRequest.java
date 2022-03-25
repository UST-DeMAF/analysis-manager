package ust.tad.analysismanager.plugin;

import java.util.Objects;

import ust.tad.analysismanager.shared.AnalysisType;

public class PluginRegistrationRequest {

    private String technology;

    private AnalysisType analysisType;

    public PluginRegistrationRequest() {
    }

    public PluginRegistrationRequest(String technology, AnalysisType analysisType) {
        this.technology = technology;
        this.analysisType = analysisType;
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

    public PluginRegistrationRequest technology(String technology) {
        setTechnology(technology);
        return this;
    }

    public PluginRegistrationRequest analysisType(AnalysisType analysisType) {
        setAnalysisType(analysisType);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PluginRegistrationRequest)) {
            return false;
        }
        PluginRegistrationRequest pluginRegistrationRequest = (PluginRegistrationRequest) o;
        return Objects.equals(technology, pluginRegistrationRequest.technology) && Objects.equals(analysisType, pluginRegistrationRequest.analysisType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(technology, analysisType);
    }

    @Override
    public String toString() {
        return "{" +
            " technology='" + getTechnology() + "'" +
            ", analysisType='" + getAnalysisType() + "'" +
            "}";
    }    
}

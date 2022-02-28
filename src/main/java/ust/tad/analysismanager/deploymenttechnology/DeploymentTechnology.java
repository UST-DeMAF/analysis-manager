package ust.tad.analysismanager.deploymenttechnology;

import java.util.Objects;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DeploymentTechnology {

    private String name;

    public DeploymentTechnology() {
    }

    public DeploymentTechnology(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeploymentTechnology name(String name) {
        setName(name);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof DeploymentTechnology)) {
            return false;
        }
        DeploymentTechnology deploymentTechnology = (DeploymentTechnology) o;
        return Objects.equals(name, deploymentTechnology.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            "}";
    }
    
}

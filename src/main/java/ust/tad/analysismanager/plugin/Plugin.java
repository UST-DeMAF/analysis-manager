package ust.tad.analysismanager.plugin;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import ust.tad.analysismanager.shared.AnalysisType;

@Entity
public class Plugin {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String technology;

  private AnalysisType analysisType;

  private String queueName;

  public Plugin() {}

  public Plugin(String technology, AnalysisType analysisType, String queueName) {
    this.technology = technology;
    this.analysisType = analysisType;
    this.queueName = queueName;
  }

  public UUID getId() {
    return this.id;
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

  public String getQueueName() {
    return this.queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public Plugin technology(String technology) {
    setTechnology(technology);
    return this;
  }

  public Plugin analysisType(AnalysisType analysisType) {
    setAnalysisType(analysisType);
    return this;
  }

  public Plugin queueName(String queueName) {
    setQueueName(queueName);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Plugin)) {
      return false;
    }
    Plugin plugin = (Plugin) o;
    return Objects.equals(id, plugin.id)
        && Objects.equals(technology, plugin.technology)
        && Objects.equals(analysisType, plugin.analysisType)
        && Objects.equals(queueName, plugin.queueName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, technology, analysisType, queueName);
  }

  @Override
  public String toString() {
    return "{"
        + " id='"
        + getId()
        + "'"
        + ", technology='"
        + getTechnology()
        + "'"
        + ", analysisType='"
        + getAnalysisType()
        + "'"
        + ", queueName='"
        + getQueueName()
        + "'"
        + "}";
  }
}

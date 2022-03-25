package ust.tad.analysismanager.plugin;

import java.util.Objects;

public class PluginRegistrationResponse {

    private String requestQueueName;

    private String responseQueueName;
    
    public PluginRegistrationResponse() {
    }

    public PluginRegistrationResponse(String requestQueueName, String responseQueueName) {
        this.requestQueueName = requestQueueName;
        this.responseQueueName = responseQueueName;
    }

    public String getRequestQueueName() {
        return this.requestQueueName;
    }

    public void setRequestQueueName(String requestQueueName) {
        this.requestQueueName = requestQueueName;
    }

    public String getResponseQueueName() {
        return this.responseQueueName;
    }

    public void setResponseQueueName(String responseQueueName) {
        this.responseQueueName = responseQueueName;
    }

    public PluginRegistrationResponse requestQueueName(String requestQueueName) {
        setRequestQueueName(requestQueueName);
        return this;
    }

    public PluginRegistrationResponse responseQueueName(String responseQueueName) {
        setResponseQueueName(responseQueueName);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PluginRegistrationResponse)) {
            return false;
        }
        PluginRegistrationResponse pluginRegistrationResponse = (PluginRegistrationResponse) o;
        return Objects.equals(requestQueueName, pluginRegistrationResponse.requestQueueName) && Objects.equals(responseQueueName, pluginRegistrationResponse.responseQueueName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestQueueName, responseQueueName);
    }

    @Override
    public String toString() {
        return "{" +
            " requestQueueName='" + getRequestQueueName() + "'" +
            ", responseQueueName='" + getResponseQueueName() + "'" +
            "}";
    }
}

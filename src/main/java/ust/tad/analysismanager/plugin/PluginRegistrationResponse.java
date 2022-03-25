package ust.tad.analysismanager.plugin;

import java.util.Objects;

public class PluginRegistrationResponse {

    private String requestQueueName;

    private String responseQueueName;

    private String responseExchangeName;

    public PluginRegistrationResponse() {
    }

    public PluginRegistrationResponse(String requestQueueName, String responseQueueName, String responseExchangeName) {
        this.requestQueueName = requestQueueName;
        this.responseQueueName = responseQueueName;
        this.responseExchangeName = responseExchangeName;
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

    public String getResponseExchangeName() {
        return this.responseExchangeName;
    }

    public void setResponseExchangeName(String responseExchangeName) {
        this.responseExchangeName = responseExchangeName;
    }

    public PluginRegistrationResponse requestQueueName(String requestQueueName) {
        setRequestQueueName(requestQueueName);
        return this;
    }

    public PluginRegistrationResponse responseQueueName(String responseQueueName) {
        setResponseQueueName(responseQueueName);
        return this;
    }

    public PluginRegistrationResponse responseExchangeName(String responseExchangeName) {
        setResponseExchangeName(responseExchangeName);
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
        return Objects.equals(requestQueueName, pluginRegistrationResponse.requestQueueName) && Objects.equals(responseQueueName, pluginRegistrationResponse.responseQueueName) && Objects.equals(responseExchangeName, pluginRegistrationResponse.responseExchangeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestQueueName, responseQueueName, responseExchangeName);
    }

    @Override
    public String toString() {
        return "{" +
            " requestQueueName='" + getRequestQueueName() + "'" +
            ", responseQueueName='" + getResponseQueueName() + "'" +
            ", responseExchangeName='" + getResponseExchangeName() + "'" +
            "}";
    }

}

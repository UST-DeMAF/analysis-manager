package ust.tad.analysismanager.transformationprocess;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import ust.tad.analysismanager.analysistask.Location;

@Service
public class ModelsService {

    @Autowired
    private WebClient modelsServiceApiClient;
    
    @Value("${models-service.url}")
    private String modelsServiceURL;

    /**
     * Sends a request to the models service for initializing the technology-specififc deployment model.
     * For that, an entity of type InitializeTechnologySpecificDeploymentModelRequest is created that contains
     * the initial information about the deployment model provided by the user and added as the request body.
     * 
     * @param transformationProcessId
     * @param technology
     * @param commands
     * @param location
     * @return the created technology-specififc deployment model as a JSON String.
     */
    public String initializeTechnologySpecificDeploymentModel(
        UUID transformationProcessId, 
        String technology, 
        List<String> commands, 
        Location location) {
        InitializeTechnologySpecificDeploymentModelRequest initializeTechnologySpecificDeploymentModelRequest = 
            new InitializeTechnologySpecificDeploymentModelRequest();
        initializeTechnologySpecificDeploymentModelRequest.setTransformationProcessId(transformationProcessId);
        initializeTechnologySpecificDeploymentModelRequest.setTechnology(technology);
        initializeTechnologySpecificDeploymentModelRequest.setCommands(commands);
        initializeTechnologySpecificDeploymentModelRequest.setLocations(List.of(location));

        return modelsServiceApiClient.post()
            .uri("/technology-specific/init")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(initializeTechnologySpecificDeploymentModelRequest))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    /**
     * Sends a request to the models service for initializing the technology-specififc deployment model.
     * The transformationProcessId is added as a query parameter.
     * 
     * @param transformationProcessId
     * @return the created technology-agnostic deployment model as a JSON String.
     */
    public String initializeTechnologyAgnosticDeploymentModel(UUID transformationProcessId) {
        return modelsServiceApiClient.post()
            .uri(uriBuilder -> uriBuilder
                .path("/technology-agnostic/init")
                .queryParam("transformationProcessId", transformationProcessId)
                .build())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .attribute("transformationProcessId", transformationProcessId)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
}

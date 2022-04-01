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
            .uri(modelsServiceURL+"/technology-specific")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(initializeTechnologySpecificDeploymentModelRequest))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
}

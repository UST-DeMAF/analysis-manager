package ust.tad.analysismanager.deploymenttechnology;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeploymentTechnologyService {
    
    @Autowired
    DeploymentTechnologyRepository repository;

    public List<DeploymentTechnology> findAll() {
        return repository.findAll();
    }

    public DeploymentTechnology findByName(String name) {
        return repository.findByName(name);
    }

    public List<DeploymentTechnology> save(Collection<DeploymentTechnology> deploymentTechnologies) {
        return repository.saveAll(deploymentTechnologies);
    }

    public DeploymentTechnology save(DeploymentTechnology deploymentTechnology) {
        return repository.save(deploymentTechnology);
    }
}

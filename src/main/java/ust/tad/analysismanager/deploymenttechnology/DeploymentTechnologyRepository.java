package ust.tad.analysismanager.deploymenttechnology;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeploymentTechnologyRepository extends MongoRepository<DeploymentTechnology, String>{
    DeploymentTechnology findByName(String name);
}

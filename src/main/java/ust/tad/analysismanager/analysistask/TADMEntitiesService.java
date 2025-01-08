package ust.tad.analysismanager.analysistask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TADMEntitiesService {

    @Autowired private TADMEntitiesRepository tadmEntitiesRepository;

    public TADMEntities saveTADMEntities(TADMEntities tadmEntities) {
        return tadmEntitiesRepository.save(tadmEntities);
    }

    public List<TADMEntities> saveAllTADMEntities(List<TADMEntities> tadmEntities) {
        return tadmEntitiesRepository.saveAll(tadmEntities);
    }

    public List<TADMEntities> createAndSaveAllTADMEntities(Map<String, List<String>> tadmEntitiesMap) {
        List<TADMEntities> tadmEntities = new ArrayList<>();
        for (var entry : tadmEntitiesMap.entrySet()) {
            tadmEntities.add(new TADMEntities(entry.getKey(), entry.getValue()));
        }
        return saveAllTADMEntities(tadmEntities);
    }
}

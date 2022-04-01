package ust.tad.analysismanager.analysistask;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import ust.tad.analysismanager.shared.AnalysisType;

@SpringBootTest(properties = {
    "spring.shell.interactive.enabled=false"
})
@EnableTransactionManagement
public class AnalysisTaskRepositoryTest {
    
    @Autowired
    LocationRepository locationRepository;
 
    @Autowired
    AnalysisTaskRepository analysisTaskRepository;

    @Test
    @Transactional("configurationsTransactionManager")
    public void whenCreatingAnalysisTask_thenCreated() throws MalformedURLException {
        Location location1 = new Location(new File("C://dm/kube").toURI().toURL(), 0, 100);
        Location location2 = new Location(new File("C://dm/kube2").toURI().toURL(), 0, 100);
        locationRepository.saveAll(List.of(location1, location2));

        AnalysisTask analysisTask = new AnalysisTask();
        analysisTask.setTransformationProcessId(UUID.randomUUID());
        analysisTask.setTechnology("Kubernetes");
        analysisTask.setAnalysisType(AnalysisType.STATIC);
        analysisTask.setCommands(List.of("command1", "command2"));
        analysisTask.setPluginQueueName("kubernetesStaticQueue");
        analysisTask.setLocations(List.of(location1, location2));

        analysisTask = analysisTaskRepository.save(analysisTask);

        assertNotNull(analysisTaskRepository.findById(analysisTask.getTaskId()));
    }
    
}

package ust.tad.analysismanager.plugin;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
public class PluginRepositoryTest {

    @Autowired
    PluginRepository pluginRepository;

    @Test
    @Transactional("configurationsTransactionManager")
    public void whenCreatingPlugin_thenCreated() {
        Plugin plugin = new Plugin();
        plugin.setTechnology("Kubernetes");
        plugin.setAnalysisType(AnalysisType.STATIC);
        plugin.setQueueName("kubernetesStaticQueue");

        plugin = pluginRepository.save(plugin);

        assertNotNull(pluginRepository.findById(plugin.getId()));
    }
    
}

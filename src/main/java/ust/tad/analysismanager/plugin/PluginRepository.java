package ust.tad.analysismanager.plugin;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ust.tad.analysismanager.shared.AnalysisType;

public interface PluginRepository extends JpaRepository<Plugin, UUID> {
  List<Plugin> findByTechnologyAndAnalysisType(String technology, AnalysisType analysisType);

  List<Plugin> findByTechnology(String technology);
}

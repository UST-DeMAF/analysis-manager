package ust.tad.analysismanager.plugin;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PluginRepository extends JpaRepository<Plugin, UUID> {
    
}

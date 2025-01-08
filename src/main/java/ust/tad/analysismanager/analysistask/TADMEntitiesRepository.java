package ust.tad.analysismanager.analysistask;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TADMEntitiesRepository extends JpaRepository<TADMEntities, UUID> {}

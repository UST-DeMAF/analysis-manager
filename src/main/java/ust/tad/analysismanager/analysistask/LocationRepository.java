package ust.tad.analysismanager.analysistask;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, UUID> {}

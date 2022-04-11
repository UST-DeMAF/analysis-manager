package ust.tad.analysismanager.analysistask;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisTaskRepository extends JpaRepository<AnalysisTask, UUID>{

    public AnalysisTask getByTaskId(UUID taskId);

    public List<AnalysisTask> getByStatus(AnalysisStatus status);
    
}

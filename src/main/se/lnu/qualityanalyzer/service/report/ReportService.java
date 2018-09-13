package se.lnu.qualityanalyzer.service.report;

import se.lnu.qualityanalyzer.model.analysis.AnalysisOutput;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisInput;

import java.util.List;

public interface ReportService {

    void create(CommitAnalysisInput commitAnalysisInput, List<? extends AnalysisOutput> analysisOutputList);

}

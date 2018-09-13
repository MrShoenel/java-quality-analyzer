package se.lnu.qualityanalyzer.service.analysis;

import org.eclipse.jgit.api.errors.GitAPIException;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisInput;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisOutput;

import java.util.List;

public interface QualityAnalyzer {

    /**
     * Analyze commits in specific branch.
     *
     * @param analysisInput
     * @return
     * @throws GitAPIException
     */
    List<CommitAnalysisOutput> analyzeCommits(CommitAnalysisInput analysisInput) throws GitAPIException;
}

package se.lnu.qualityanalyzer.converter;

import se.lnu.qualityanalyzer.enums.FileStatus;
import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisOutput;
import se.lnu.qualityanalyzer.model.report.CommitAnalysisReport;
import se.lnu.qualityanalyzer.util.AnalysisUtil;

import java.util.function.Function;
import java.util.stream.Collectors;

public class CommitAnalysisOutputToCommitAnalysisReportConverter implements Function<CommitAnalysisOutput, CommitAnalysisReport> {

    @Override
    public CommitAnalysisReport apply(CommitAnalysisOutput commitAnalysisOutput) {
        CommitAnalysisReport commitAnalysisReport = new CommitAnalysisReport();

        commitAnalysisReport.setSha(commitAnalysisOutput.getCommitInfo().getSha());
        commitAnalysisReport.setDate(commitAnalysisOutput.getCommitInfo().getDate());
        commitAnalysisReport.setAuthorEmail(commitAnalysisOutput.getCommitInfo().getAuthor().getEmail());
        commitAnalysisReport.setAuthorName(commitAnalysisOutput.getCommitInfo().getAuthor().getName());
        commitAnalysisReport.setMessageLength(commitAnalysisOutput.getCommitInfo().getMessageLength());
        commitAnalysisReport.setCommentCount(commitAnalysisOutput.getCommitInfo().getCommentCount());

        commitAnalysisReport.setAdditions(commitAnalysisOutput.getCommitInfo().getStatistics().getAdditions());
        commitAnalysisReport.setDeletions(commitAnalysisOutput.getCommitInfo().getStatistics().getDeletions());
        commitAnalysisReport.setTotalChanges(commitAnalysisOutput.getCommitInfo().getStatistics().getTotal());

        commitAnalysisReport.setAddedFiles(commitAnalysisOutput.getCommitInfo().getFiles()
                .stream().filter(f -> f.getStatus().equals(FileStatus.ADDED)).collect(Collectors.toList()).size());
        commitAnalysisReport.setModifiedFiles(commitAnalysisOutput.getCommitInfo().getFiles()
                .stream().filter(f -> f.getStatus().equals(FileStatus.MODIFIED)).collect(Collectors.toList()).size());
        commitAnalysisReport.setRemovedFiles(commitAnalysisOutput.getCommitInfo().getFiles()
                .stream().filter(f -> f.getStatus().equals(FileStatus.REMOVED)).collect(Collectors.toList()).size());
        commitAnalysisReport.setTotalFiles(commitAnalysisOutput.getCommitInfo().getFiles().size());

        if (commitAnalysisOutput.getMetrics().size() > 0) {
            commitAnalysisReport.setCohesion(commitAnalysisOutput.getMetrics().get(MetricName.COHESION).getValue());
            commitAnalysisReport.setSize(commitAnalysisOutput.getMetrics().get(MetricName.SIZE).getValue());
            commitAnalysisReport.setComplexity(commitAnalysisOutput.getMetrics().get(MetricName.COMPLEXITY).getValue());
            commitAnalysisReport.setHierarchy(commitAnalysisOutput.getMetrics().get(MetricName.HIERARCHY).getValue());
            commitAnalysisReport.setQuality(commitAnalysisOutput.getMetrics().get(MetricName.QUALITY).getValue());
            commitAnalysisReport.setCoupling(commitAnalysisOutput.getMetrics().get(MetricName.COUPLING).getValue());
            commitAnalysisReport.setCloning(commitAnalysisOutput.getMetrics().get(MetricName.CLONING).getValue());
            commitAnalysisReport.setReadability(commitAnalysisOutput.getMetrics().get(MetricName.READABILITY).getValue());
            commitAnalysisReport.setTesting(commitAnalysisOutput.getMetrics().get(MetricName.TESTING).getValue());

            commitAnalysisReport.setTotalQuality(AnalysisUtil.getTotalQuality(commitAnalysisOutput.getMetrics()));
        }

        return commitAnalysisReport;
    }

}

package se.lnu.qualityanalyzer.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.model.git.CommitInfo;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CommitAnalysisOutput extends AnalysisOutput {

    private CommitInfo commitInfo;

    private Map<MetricName, Metric> metrics;

}

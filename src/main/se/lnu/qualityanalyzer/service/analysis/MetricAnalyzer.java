package se.lnu.qualityanalyzer.service.analysis;

import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.model.analysis.Metric;

import java.util.Map;

public interface MetricAnalyzer {

    Map<MetricName, Metric> analyze(String sourcePath);

}

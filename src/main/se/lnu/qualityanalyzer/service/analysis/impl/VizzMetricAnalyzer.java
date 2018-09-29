package se.lnu.qualityanalyzer.service.analysis.impl;

import grail.util.Debug;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.AnalysisHandler;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.AnalysisInterface;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.AnalysisType;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.parameters.ParametersCollection;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.parameters.ParametersCollectionSerializer;
import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.model.analysis.Metric;
import se.lnu.qualityanalyzer.service.analysis.MetricAnalyzer;
import se.lnu.qualityanalyzer.util.AnalysisUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class VizzMetricAnalyzer implements MetricAnalyzer {

    @Override
    public Map<MetricName, Metric> analyze(String sourcePath) {
        Debug.setDebug(true);
        Debug.setOutput(true);

        AnalysisInterface analysis = new AnalysisHandler();
        analysis.setAnalysisMessageHandler((type, message, e) -> { });
        analysis.setProgressChangeHandler(args -> args.setCancel(true));

        File sourcePathFile = new File(sourcePath);
        if (!sourcePathFile.exists())
            throw new Error("Directory " + sourcePath + " does not exist.");
        else {
            ParametersCollection params = null;
            params = ParametersCollectionSerializer.deserialize(AnalysisType.JAVA);

            analysis.analyze(AnalysisType.JAVA, sourcePathFile, params);

            AnalysisUtil.printMetricsIntoFile(analysis, params);
            AnalysisUtil.printRootMetrics(analysis, AnalysisType.JAVA);
        }

        return AnalysisUtil.mapRootMetrics(analysis, AnalysisType.JAVA);
    }
}

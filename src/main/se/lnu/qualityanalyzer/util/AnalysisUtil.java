package se.lnu.qualityanalyzer.util;

import se.arisa.vizzanalyzer.frontends.QualityMonitor.AnalysisInterface;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.AnalysisType;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.measurements.Measurement;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.measurements.Metrics;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.measurements.MetricsFactory;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.parameters.ParametersCollection;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.parameters.ParametersCollectionSerializer;
import se.arisa.vizzanalyzer.frontends.reporting.MetricsOutput;
import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.model.analysis.Metric;

import java.util.*;

public final class AnalysisUtil {

    public static List<Metric> listRootMetrics(AnalysisInterface analysisHandler, AnalysisType analysisType) {
        List<Metric> result = new ArrayList<>();

        MetricsFactory mf = analysisType.getMetricsFactory();
        Measurement measurement = analysisHandler.getMeasurement(AnalysisInterface.EntryType.UPLOAD, null);

        for (Metrics metric : mf.getAllRootMetrics()) {
            MetricName metricName = VizzAnalyzerUtil.getMetricName(metric.getMetricName());
            result.add(new Metric(metricName, measurement.getMetricsValue(metric)));
        }

        return result;
    }

    public static Map<MetricName, Metric> mapRootMetrics(AnalysisInterface analysisHandler, AnalysisType analysisType) {
        Map<MetricName, Metric> result = new HashMap<>();

        MetricsFactory mf = analysisType.getMetricsFactory();
        Measurement measurement = analysisHandler.getMeasurement(AnalysisInterface.EntryType.PROGRAM,
                analysisHandler.getPrograms().entrySet().iterator().next().getKey());

        for (Metrics metric : mf.getAllRootMetrics()) {
            MetricName metricName = VizzAnalyzerUtil.getMetricName(metric.getMetricName());
            result.put(metricName, new Metric(metricName, measurement.getMetricsValue(metric)));
        }

        return result;
    }

    public static void printMetricsIntoFile(AnalysisInterface analysisHandler, ParametersCollection params) {
        MetricsOutput out = new MetricsOutput(analysisHandler, AnalysisType.JAVA);
        out.printMetricsValues4Projects();
        out.printPrograms();
        if (params == null) {
            ParametersCollectionSerializer.serialize(analysisHandler.getParameters(), AnalysisType.JAVA);
        }
    }

    public static void printPublicMetrics(AnalysisInterface analysisHandler, AnalysisType analysisType) {
        MetricsFactory mf = analysisType.getMetricsFactory();
        Measurement measurement = analysisHandler.getMeasurement(AnalysisInterface.EntryType.UPLOAD, null);
        printMetrics(mf.getAllPublicMetrics(), measurement);
    }

    public static void printRootMetrics(AnalysisInterface analysisHandler, AnalysisType analysisType) {
        MetricsFactory mf = analysisType.getMetricsFactory();
        Measurement measurement = analysisHandler.getMeasurement(AnalysisInterface.EntryType.PROGRAM,
                analysisHandler.getPrograms().entrySet().iterator().next().getKey());
        printMetrics(mf.getAllRootMetrics(), measurement);
    }

    private static void printMetrics(Collection<Metrics> metrics, Measurement measurement) {
        System.out.println("\nResult of quality analysis - KPI's:");
        for (Metrics metric : metrics) {
            System.out.print(metric.getMetricName());
            System.out.println(" = " + measurement.getMetricsValue(metric));
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public static Double getTotalQuality(Map<MetricName, Metric> metrics) {
        Double result = 0.0;

        for (Map.Entry<MetricName, Metric> entry : metrics.entrySet()) {
            result += entry.getValue().getValue();
        }

        return result / metrics.size();
    }

}

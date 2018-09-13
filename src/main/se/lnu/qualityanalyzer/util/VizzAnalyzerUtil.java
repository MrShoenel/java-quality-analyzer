package se.lnu.qualityanalyzer.util;

import se.lnu.qualityanalyzer.enums.MetricName;

public final class VizzAnalyzerUtil {

    public static MetricName getMetricName(String vizzAnalyzerMetricName) {
        switch (vizzAnalyzerMetricName) {
            case "Cohesion" :
                return MetricName.COHESION;
            case "Size" :
                return MetricName.SIZE;
            case "Complexity" :
                return MetricName.COMPLEXITY;
            case "Hierarchy" :
                return MetricName.HIERARCHY;
            case "Quality" :
                return MetricName.QUALITY;
            case "Coupling" :
                return MetricName.COUPLING;
            case "Cloning issues" :
                return MetricName.CLONING;
            case "Readability" :
                return MetricName.READABILITY;
            case "Testing issues" :
                return MetricName.TESTING;
            default:
                throw new IllegalArgumentException("Invalid metric's name: " + vizzAnalyzerMetricName);
        }
    }

}

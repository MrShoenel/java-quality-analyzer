package se.lnu.qualityanalyzer.util;

import com.google.gson.GsonBuilder;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.AnalysisInterface;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.AnalysisType;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.measurements.Measurement;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.measurements.Metrics;
import se.arisa.vizzanalyzer.frontends.QualityMonitor.measurements.MetricsFactory;
import se.lnu.qualityanalyzer.enums.OutputEntityType;

import java.util.*;
import java.util.stream.Collectors;

public class JsonOutput {
    protected transient final AnalysisInterface analysisHandler;

    protected transient final AnalysisType analysisType;

    protected transient final MetricsFactory metricsFactory;

    protected transient Map<Metrics, String> metrics, metricsScores;

    protected transient Map<OutputEntityType, String> outputEntityTypes;

    protected transient Map<String, Metrics> metricsById, metricsScoreById;

    protected Map<String, OutputEntityType> outputEntityTypesById;

    protected Collection<Metrics> allMetrics;

    protected Map<String, String> allMetricsById;

    protected List<OutputEntity> entities;

    public JsonOutput(AnalysisInterface analysisHandler, AnalysisType analysisType) {
        this.analysisHandler = analysisHandler;
        this.analysisType = analysisType;
        this.metricsFactory = this.analysisType.getMetricsFactory();

        this.initializeMetrics();
        this.initializeOutputEntityTypes();
        this.entities = new ArrayList<>();
    }

    protected final void initializeMetrics() {
        this.metricsById = new TreeMap<>();
        this.metricsScoreById = new TreeMap<>();
        this.allMetrics = this.metricsFactory.getAllPublicMetrics();
        this.allMetricsById = new TreeMap<>();

        Integer i = 0;
        for (Metrics m : this.allMetrics) {
            this.metricsById.put(String.format("%03d", i), m);
            this.allMetricsById.put(String.format("%03d", i), m.getMetricName());

            i++;
            this.metricsScoreById.put(String.format("%03d", i) + "_s", m);
            this.allMetricsById.put(String.format("%03d", i) + "_s", m.getMetricName() + " (score)");

            i++;
        }

        this.metrics = this.metricsById
            .entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        this.metricsScores = this.metricsScoreById
            .entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    protected final void initializeOutputEntityTypes() {
        this.outputEntityTypesById = new HashMap<>();

        Integer i = 0;
        for (OutputEntityType oet : OutputEntityType.values()) {
            this.outputEntityTypesById.put(String.format("%03d", i++), oet);
        }

        this.outputEntityTypes = this.outputEntityTypesById
            .entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private OutputEntity fillEntity(OutputEntity entity, Measurement measurement, Collection<Metrics> metrics) {
        entity.metricsValues.clear();

        for (Metrics m : metrics) {
            if (entity.type == OutputEntityType.UPLOAD.type) {
                // For type upload, there is no score
                entity.metricsValues.put(this.metrics.get(m), measurement.getMetricsValue(m));
            } else if (entity.type == OutputEntityType.FILE.type) {
                // For files, we also put a score
                Measurement.Measurement4Metrics m4m = measurement.getMeasurement4Metrics(m);
                if (m4m == null) {
                    continue;
                }

                if (m4m.getAbsoluteValue() < 0.) {
                    entity.metricsValues.put(this.metrics.get(m), -1d);
                    entity.metricsValues.put(this.metricsScores.get(m), 1d);
                } else {
                    entity.metricsValues.put(this.metrics.get(m), m4m.getAbsoluteValue());
                    entity.metricsValues.put(this.metricsScores.get(m), m4m.getScore());
                }
            }
        }

        return entity;
    }

    public JsonOutput fill() {
        this.entities.clear();

        final Measurement uploadMeasurement = this.analysisHandler.getMeasurement(
            AnalysisInterface.EntryType.UPLOAD, null);
        this.entities.add(this.fillEntity(
            new OutputEntity(OutputEntityType.UPLOAD, "upload"),
            uploadMeasurement,
            this.metricsFactory.getAllPublicMetrics())
        );


        final List<String> files = this.analysisHandler.getPrograms()
            .values().stream().flatMap(strings -> strings.stream()).collect(Collectors.toList());
        for (String file : files) {
            final Measurement fileMeasurement = this.analysisHandler.getMeasurement(
                AnalysisInterface.EntryType.FILE, file);
            this.entities.add(this.fillEntity(
                new OutputEntity(OutputEntityType.FILE, file),
                fileMeasurement,
                this.metricsFactory.getAllPublicMetrics())
            );
        }

        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }


    protected class OutputEntity {
        protected final int type;

        protected final String name;

        protected final Map<String, Double> metricsValues;

        public OutputEntity(OutputEntityType type, String name) {
            this.type = type.type;
            this.name = name;
            this.metricsValues = new TreeMap<>();
        }
    }
}


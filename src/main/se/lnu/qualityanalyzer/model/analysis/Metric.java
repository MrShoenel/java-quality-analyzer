package se.lnu.qualityanalyzer.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import se.lnu.qualityanalyzer.enums.MetricName;

@Getter
@Setter
@AllArgsConstructor
public class Metric {

    private MetricName name;

    // Score (not absolute value)
    private Double value;

    @Override
    public String toString() {
        return new org.apache.commons.lang.builder.ToStringBuilder(this)
                .append("Metric's name", name.toString())
                .append("Metric's value", value)
                .toString();
    }
}

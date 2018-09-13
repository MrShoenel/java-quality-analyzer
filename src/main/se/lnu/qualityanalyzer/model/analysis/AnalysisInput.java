package se.lnu.qualityanalyzer.model.analysis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.lnu.qualityanalyzer.enums.CVSType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisInput {

    private CVSType cvsType;

    private String repositoryName;

    private String repositoryUrl;

    private String repositoryOwner;

}

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
public class CommitAnalysisInput extends AnalysisInput {

    public CommitAnalysisInput(CVSType cvsType, String repositoryName, String repositoryUrl, String repositoryOwner,
                               String branchName, int numberOfCommits, int offsetOfCommits) {
        super(cvsType, repositoryName, repositoryUrl, repositoryOwner);
        this.branchName = branchName;
        this.numberOfCommits = numberOfCommits;
        this.offsetOfCommits = offsetOfCommits;
    }

    private String branchName;

    // If == 0, then all commits will be analyzed.
    private int numberOfCommits;

    // If == 0, then analysis will start on first commit.
    private int offsetOfCommits;

}

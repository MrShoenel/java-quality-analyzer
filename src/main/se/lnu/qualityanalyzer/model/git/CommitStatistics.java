package se.lnu.qualityanalyzer.model.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitStatistics {

    private int additions;

    private int deletions;

    private int total;

}

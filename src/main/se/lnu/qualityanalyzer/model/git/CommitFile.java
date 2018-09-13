package se.lnu.qualityanalyzer.model.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.lnu.qualityanalyzer.enums.FileStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitFile {

    private int additions;

    private int changes;

    private int deletions;

    private FileStatus status;

}

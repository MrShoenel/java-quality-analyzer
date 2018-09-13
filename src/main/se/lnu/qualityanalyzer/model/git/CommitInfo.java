package se.lnu.qualityanalyzer.model.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitInfo {

    private String sha;

    private Date date;

    private CommitAuthor author;

    private int messageLength;

    private int commentCount;

    private CommitStatistics statistics;

    private List<CommitFile> files;

}

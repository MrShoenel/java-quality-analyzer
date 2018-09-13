package se.lnu.qualityanalyzer.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommitAnalysisReport extends AnalysisReport {

    // Info about commit

    private String sha;

    private Date date;

    private String authorEmail;

    private String authorName;

    private int messageLength;

    private int commentCount;

    private int additions;

    private int deletions;

    private int totalChanges;

    private int addedFiles;

    private int modifiedFiles;

    private int removedFiles;

    private int totalFiles;

    // Info about metrics

    private double cohesion;

    private double size;

    private double complexity;

    private double hierarchy;

    private double quality;

    private double coupling;

    private double cloning;

    private double readability;

    private double testing;

    private double totalQuality;

}

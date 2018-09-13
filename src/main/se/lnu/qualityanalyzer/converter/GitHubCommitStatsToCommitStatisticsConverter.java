package se.lnu.qualityanalyzer.converter;

import org.eclipse.egit.github.core.CommitStats;
import org.springframework.beans.BeanUtils;
import se.lnu.qualityanalyzer.model.git.CommitStatistics;

import java.util.function.Function;

public class GitHubCommitStatsToCommitStatisticsConverter implements Function<CommitStats, CommitStatistics> {

    @Override
    public CommitStatistics apply(CommitStats commitStats) {
        CommitStatistics commitStatistics = new CommitStatistics();
        BeanUtils.copyProperties(commitStats, commitStatistics);
        return commitStatistics;
    }

}

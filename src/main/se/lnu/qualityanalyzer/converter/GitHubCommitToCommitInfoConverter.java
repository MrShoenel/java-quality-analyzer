package se.lnu.qualityanalyzer.converter;

import org.eclipse.egit.github.core.RepositoryCommit;
import se.lnu.qualityanalyzer.model.git.CommitInfo;

import java.util.function.Function;
import java.util.stream.Collectors;

public class GitHubCommitToCommitInfoConverter implements Function<RepositoryCommit, CommitInfo> {

    private GitHubCommitUserToCommitAuthorConverter authorConverter;

    private GitHubCommitStatsToCommitStatisticsConverter statisticsConverter;

    private GitHubCommitFileToCommitFileConverter fileConverter;

    public GitHubCommitToCommitInfoConverter() {
        this.authorConverter = new GitHubCommitUserToCommitAuthorConverter();
        this.statisticsConverter = new GitHubCommitStatsToCommitStatisticsConverter();
        this.fileConverter = new GitHubCommitFileToCommitFileConverter();
    }

    @Override
    public CommitInfo apply(RepositoryCommit repositoryCommit) {
        CommitInfo commitInfo = new CommitInfo();

        commitInfo.setSha(repositoryCommit.getSha());
        commitInfo.setDate(repositoryCommit.getCommit().getAuthor().getDate());
        commitInfo.setAuthor(authorConverter.apply(repositoryCommit.getCommit().getAuthor()));
        commitInfo.setMessageLength(repositoryCommit.getCommit().getMessage().length());
        commitInfo.setCommentCount(repositoryCommit.getCommit().getCommentCount());
        commitInfo.setStatistics(statisticsConverter.apply(repositoryCommit.getStats()));
        commitInfo.setFiles(repositoryCommit.getFiles().stream().map(f -> fileConverter.apply(f)).collect(Collectors.toList()));

        return commitInfo;
    }

}

package se.lnu.qualityanalyzer.service.git.connector.impl;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import se.lnu.qualityanalyzer.converter.GitHubCommitToCommitInfoConverter;
import se.lnu.qualityanalyzer.model.git.CommitInfo;
import se.lnu.qualityanalyzer.service.git.connector.GitConnector;

import java.io.IOException;

public class GitHubConnectorImpl implements GitConnector {

    private RepositoryService repositoryService;

    private CommitService commitService;

    private GitHubCommitToCommitInfoConverter commitConverter;

    public GitHubConnectorImpl() {
        this.repositoryService = new RepositoryService();
        this.commitService = new CommitService();
        this.commitConverter = new GitHubCommitToCommitInfoConverter();
    }

    @Override
    public CommitInfo getCommit(String repositoryName, String repositoryOwner, String commitSha) {
        try {
            Repository repository = repositoryService.getRepository(repositoryOwner,repositoryName);
            RepositoryCommit repositoryCommit = commitService.getCommit(repository, commitSha);
            return commitConverter.apply(repositoryCommit);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}

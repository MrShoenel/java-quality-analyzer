package se.lnu.qualityanalyzer.service.git.connector;

import se.lnu.qualityanalyzer.model.git.CommitInfo;

public interface GitConnector {

    CommitInfo getCommit(String repositoryName, String repositoryOwner, String commitSha);

}

package se.lnu.qualityanalyzer.service.git.connector.impl;

import se.lnu.qualityanalyzer.model.git.CommitInfo;
import se.lnu.qualityanalyzer.service.git.connector.GitConnector;

/**
 * TODO Implement
 */
public class BitbucketConnector implements GitConnector {

    @Override
    public CommitInfo getCommit(String repositoryName, String repositoryOwner, String commitSha) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

}

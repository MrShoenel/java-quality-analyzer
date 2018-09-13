package se.lnu.qualityanalyzer.converter;

import se.lnu.qualityanalyzer.model.git.CommitFile;
import se.lnu.qualityanalyzer.util.GitHubUtil;

import java.util.function.Function;

public class GitHubCommitFileToCommitFileConverter implements Function<org.eclipse.egit.github.core.CommitFile, CommitFile> {

    @Override
    public CommitFile apply(org.eclipse.egit.github.core.CommitFile gitHubCommitFile) {
        CommitFile commitFile = new CommitFile();

        commitFile.setAdditions(gitHubCommitFile.getAdditions());
        commitFile.setChanges(gitHubCommitFile.getChanges());
        commitFile.setDeletions(gitHubCommitFile.getDeletions());
        commitFile.setStatus(GitHubUtil.getFileStatus(gitHubCommitFile.getStatus()));

        return commitFile;
    }

}

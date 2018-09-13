package se.lnu.qualityanalyzer.service.git;

import org.eclipse.jgit.revwalk.RevCommit;
import se.lnu.qualityanalyzer.exception.GitException;
import se.lnu.qualityanalyzer.model.git.CommitInfo;
import se.lnu.qualityanalyzer.model.git.GitRepository;

import java.util.List;

public interface GitService {

    /**
     * Clone Git repository into local temp folder.
     *
     * @param repositoryUrl
     * @param targetBranch
     * @return
     * @throws GitException
     */
    GitRepository cloneRepository(String repositoryUrl, String targetBranch) throws GitException;

    boolean removeLocalRepository(String localPath);

    List<RevCommit> listOfCommits(GitRepository gitRepository, String targetBranch) throws GitException;

    CommitInfo getCommitInfo(String repositoryName, String repositoryOwner, String commitSha);

}

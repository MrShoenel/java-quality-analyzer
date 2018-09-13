package se.lnu.qualityanalyzer.service.git.impl;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import se.lnu.qualityanalyzer.exception.GitException;
import se.lnu.qualityanalyzer.model.git.CommitInfo;
import se.lnu.qualityanalyzer.model.git.GitRepository;
import se.lnu.qualityanalyzer.service.git.connector.GitConnector;
import se.lnu.qualityanalyzer.service.git.GitService;
import se.lnu.qualityanalyzer.service.git.connector.impl.GitHubConnectorImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GitServiceImpl implements GitService {

    private GitConnector gitConnector;

    public GitServiceImpl() {
        this.gitConnector = new GitHubConnectorImpl();
    }

    @Override
    public GitRepository cloneRepository(String repositoryUrl, String targetBranch) throws GitException {
        Git git;
        File localPath;

        try {
            localPath = File.createTempFile("_QualityAnalyzer_TempRepositoryFolder_", "");

            if(!localPath.delete()) {
                throw new IOException("Could not delete temporary file " + localPath);
            }

            System.out.println("Cloning Git repository from " + repositoryUrl + " to " + localPath);

            git = Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(localPath)
                    .setBranchesToClone(Arrays.asList("refs/heads/" + targetBranch))
                    .setBranch("refs/heads/" + targetBranch)
                    .call();

            // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
            System.out.println("Having Git repository in " + git.getRepository().getDirectory());
            git.getRepository().close();
        } catch (IOException | GitAPIException | NullPointerException e) {
            throw new GitException(e);
        }

        return new GitRepository(git.getRepository(), localPath.getAbsolutePath());
    }

    @Override
    public boolean removeLocalRepository(String localPath) {
        try {
            FileUtils.deleteDirectory(new File(localPath));
            System.out.println("Git repository by path: " + localPath + " was removed.");
            return true;
        } catch (IOException | NullPointerException e) {
            System.out.println("Git repository by path: " + localPath + " was not removed.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<RevCommit> listOfCommits(GitRepository gitRepository, String targetBranch) throws GitException {
        Git git = new Git(gitRepository.getRepository());

        List<RevCommit> result = new ArrayList<>();
        try {
            Iterable<RevCommit> commits = git.log()
                    .add(git.getRepository().resolve("remotes/origin/" + targetBranch))
                    .call();
            for (RevCommit commit : commits) {
                result.add(commit);
            }
        } catch(IOException | GitAPIException e) {
            throw new GitException(e);
        }

        return result;
    }

    @Override
    public CommitInfo getCommitInfo(String repositoryName, String repositoryOwner, String commitSha) {
        return gitConnector.getCommit(repositoryName, repositoryOwner, commitSha);
    }

}

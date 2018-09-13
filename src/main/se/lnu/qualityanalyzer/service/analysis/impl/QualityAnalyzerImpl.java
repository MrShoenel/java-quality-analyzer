package se.lnu.qualityanalyzer.service.analysis.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.lnu.qualityanalyzer.Application;
import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.exception.GitException;
import se.lnu.qualityanalyzer.exception.MavenException;
import se.lnu.qualityanalyzer.model.analysis.*;
import se.lnu.qualityanalyzer.model.git.CommitInfo;
import se.lnu.qualityanalyzer.model.git.GitRepository;
import se.lnu.qualityanalyzer.service.analysis.QualityAnalyzer;
import se.lnu.qualityanalyzer.service.git.impl.GitServiceImpl;
import se.lnu.qualityanalyzer.service.maven.MavenService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QualityAnalyzerImpl implements QualityAnalyzer {

    private GitServiceImpl gitServiceImpl;

    private MavenService mavenService;

    private VizzAnalyzer vizzAnalyzer;

    private final static Logger log = LoggerFactory.getLogger(Application.class);

    private CommitAnalysisInput commitAnalysisInput;

    public QualityAnalyzerImpl() {
        this.gitServiceImpl = new GitServiceImpl();
        this.mavenService = new MavenService();
        this.vizzAnalyzer = new VizzAnalyzer();
    }

    /**
     * Main entry point ot run the analysis.
     *
     * @param commitAnalysisInput
     * @return CommitAnalysisOutput or null in case of exception
     * @throws GitAPIException
     */
    @Override
    public List<CommitAnalysisOutput> analyzeCommits(CommitAnalysisInput commitAnalysisInput) throws GitAPIException {
        this.commitAnalysisInput = commitAnalysisInput;
        GitRepository gitRepository = null;

        try {
            gitRepository = gitServiceImpl.cloneRepository(commitAnalysisInput.getRepositoryUrl(), commitAnalysisInput.getBranchName());
            List<RevCommit> commitList = gitServiceImpl.listOfCommits(gitRepository, commitAnalysisInput.getBranchName());

            return analyzeCommits(gitRepository, commitList);
        } catch (MavenException e) {
            log.error("Maven build failure!");
            e.printStackTrace();
        } catch (GitException e) {
            log.error("Git repository is empty!");
            e.printStackTrace();
        } finally {
            gitServiceImpl.removeLocalRepository(gitRepository.getAbsolutePath());
        }

        return null;
    }

    /**
     * Analyze quality in list of commits.
     *
     * @param gitRepository
     * @param commitList
     * @return
     * @throws GitAPIException
     * @throws MavenException
     */
    private List<CommitAnalysisOutput> analyzeCommits(GitRepository gitRepository, List<RevCommit> commitList) throws GitAPIException, MavenException {
        List<CommitAnalysisOutput> result = new LinkedList<>();

        int analyzedCommitsCount = 0;

        for (RevCommit commit : commitList) {
            if (commitAnalysisInput.getOffsetOfCommits() <= commitList.indexOf(commit)) {
                if (commitAnalysisInput.getNumberOfCommits() != 0 && analyzedCommitsCount >= commitAnalysisInput.getNumberOfCommits()) {
                    break;
                }

                result.add(analyzeCommit(gitRepository, commit));
                analyzedCommitsCount++;
            }
        }

        return result;
    }

    /**
     * Analyze quality in one specific commit.
     *
     * @param gitRepository
     * @param commit
     * @return
     * @throws GitAPIException
     * @throws MavenException
     */
    private CommitAnalysisOutput analyzeCommit(GitRepository gitRepository, RevCommit commit) throws GitAPIException, MavenException {
        Git git = new Git(gitRepository.getRepository());
        git.checkout()
                .setCreateBranch(true)
                .setName("_QualityAnalyzer_TempRepositoryBranch_" + commit.getName())
                .setStartPoint(commit.getName())
                .call();

        System.out.println("Checked out on branch: " + "_QualityAnalyzer_TempRepositoryBranch_" + commit.getName());

        mavenService.run(gitRepository.getAbsolutePath());
        Map<MetricName, Metric> commitMetrics = vizzAnalyzer.analyze(gitRepository.getAbsolutePath());

        CommitInfo commitInfo = gitServiceImpl.getCommitInfo(commitAnalysisInput.getRepositoryName(), commitAnalysisInput.getRepositoryOwner(), commit.getName());

        return new CommitAnalysisOutput(commitInfo, commitMetrics);
    }

}

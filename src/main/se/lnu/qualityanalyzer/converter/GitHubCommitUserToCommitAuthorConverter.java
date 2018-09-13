package se.lnu.qualityanalyzer.converter;

import org.eclipse.egit.github.core.CommitUser;
import org.springframework.beans.BeanUtils;
import se.lnu.qualityanalyzer.model.git.CommitAuthor;

import java.util.function.Function;

public class GitHubCommitUserToCommitAuthorConverter implements Function<CommitUser, CommitAuthor> {

    @Override
    public CommitAuthor apply(CommitUser commitUser) {
        CommitAuthor commitAuthor = new CommitAuthor();
        BeanUtils.copyProperties(commitUser, commitAuthor);
        return commitAuthor;
    }

}

package se.lnu.qualityanalyzer.model.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.lib.Repository;

@Getter
@Setter
@AllArgsConstructor
public class GitRepository {

    private Repository repository;

    // repositoryLocalPath
    private String absolutePath;

}

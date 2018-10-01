package se.lnu.qualityanalyzer.service.build;

import se.lnu.qualityanalyzer.model.git.GitRepository;

public interface Service {
    public void build(GitRepository repository);

}

package se.lnu.qualityanalyzer.service.build;

import se.lnu.qualityanalyzer.model.git.GitRepository;

import java.io.File;

public class GradleService implements Service {
    public static final boolean IsWindows = System.getProperty("os.name").toLowerCase().contains("windows");

    @Override
    public void build(GitRepository repository) {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{
                new File(repository.getAbsolutePath() + File.separator + "gradlew" + (GradleService.IsWindows ? ".bat" : "")).getAbsolutePath(),
                "clean",
                "build"
            });
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}

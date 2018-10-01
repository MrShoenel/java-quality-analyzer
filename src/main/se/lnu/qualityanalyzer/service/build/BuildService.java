package se.lnu.qualityanalyzer.service.build;

import se.lnu.qualityanalyzer.enums.ProjectType;
import se.lnu.qualityanalyzer.model.git.GitRepository;

import java.io.File;


/**
 * @author Sebastian Hönel
 */
public class BuildService {
    public static final Service
            GradleBuildService = new GradleService()
            , MavenBuildService = new MavenService();

    public static void build(GitRepository repository) {
        final ProjectType pt = BuildService.getProjectType(repository);
        Service buildService = null;

        switch (pt) {
            case Gradle:
                buildService = BuildService.GradleBuildService;
                break;
            case Maven:
                buildService = BuildService.MavenBuildService;
                break;
            case Unknown:
            default:
                throw new Error("The Project-Type " + pt.toString() + " is not supported.");

        }

        buildService.build(repository);
    }

    /**
     * Determines if a GitRepository contains a Maven- or a Gradle-project.
     * Returns 'Unknown' if neither of these.
     *
     * @param repository
     * @return ProjectType
     */
    public static ProjectType getProjectType(GitRepository repository) {
        File temp = new File(repository.getAbsolutePath() + File.separator + "pom.xml");
        if (temp.exists()) {
            return ProjectType.Maven;
        }

        temp = new File(repository.getAbsolutePath() + File.separator + "gradlew");
        if (temp.exists()) {
            return ProjectType.Gradle;
        }

        return ProjectType.Unknown;
    }
}

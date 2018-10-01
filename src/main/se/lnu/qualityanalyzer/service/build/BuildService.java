package se.lnu.qualityanalyzer.service.build;

import se.lnu.qualityanalyzer.enums.ProjectType;
import java.io.File;


/**
 * @author Sebastian Hönel
 */
public class BuildService {
    public static final Service
            GradleBuildService = new GradleService()
            , MavenBuildService = new MavenService();

    public static void build(File projectDir) {
        final ProjectType pt = BuildService.getProjectType(projectDir);
        Service buildService;

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

        buildService.build(projectDir);
    }

    /**
     * Determines if a GitRepository contains a Maven- or a Gradle-project.
     * Returns 'Unknown' if neither of these.
     *
     * @param projectDir
     * @return ProjectType
     */
    public static ProjectType getProjectType(File projectDir) {
        File temp = new File(projectDir.getAbsolutePath() + File.separator + "pom.xml");
        if (temp.exists()) {
            return ProjectType.Maven;
        }

        temp = new File(projectDir.getAbsolutePath() + File.separator + "gradlew");
        if (temp.exists()) {
            return ProjectType.Gradle;
        }

        return ProjectType.Unknown;
    }
}

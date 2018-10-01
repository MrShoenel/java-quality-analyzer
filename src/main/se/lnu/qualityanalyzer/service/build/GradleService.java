package se.lnu.qualityanalyzer.service.build;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class GradleService implements Service {
    public static final boolean IsWindows = System.getProperty("os.name").toLowerCase().contains("windows");

    @Override
    public void build(File projectDir) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    new File(
                            projectDir.getAbsolutePath() + File.separator + "gradlew" + (GradleService.IsWindows ? ".bat" : "")).getAbsolutePath(),
                    "clean",
                    "build");
            pb.directory(projectDir);
            Process proc = pb.start();

            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String line;
            StringBuilder b = new StringBuilder();
            while ((line = input.readLine()) != null) {
                b.append(line);
            }

            final int exitCode = proc.waitFor();
            if (exitCode != 0) {
                throw new Error("Gradle failed with exit-code " + exitCode + " and message: " + b.toString());
            }
        } catch (Throwable t) {
            throw new Error(t);
        }
    }
}

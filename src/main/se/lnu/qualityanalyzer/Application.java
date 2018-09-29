package se.lnu.qualityanalyzer;

import com.google.gson.Gson;
import org.eclipse.jgit.api.Git;
import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.model.analysis.Metric;
import se.lnu.qualityanalyzer.model.git.GitRepository;
import se.lnu.qualityanalyzer.service.analysis.impl.VizzMetricAnalyzer;
import se.lnu.qualityanalyzer.service.maven.MavenService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;

public class Application {
    private static final PrintStream
            stdout = System.out
            , stderr = System.err
            , dummy = new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException { }
            });

    /**
     * Enables or disables printing to stdout/stderr globally. We use this to prevent
     * output of 3rd party code that we cannot control.
     *
     * @param enable true if enabling; false, otherwise
     */
    public static void toggleSysout(boolean enable) {
        if (enable) {
            System.setOut(stdout);
            System.setErr(stderr);
        } else {
            System.setOut(dummy);
            System.setErr(dummy);
        }
    }

    public static void main(String[] args) {
        double version = Double.parseDouble(System.getProperty("java.specification.version"));
        if (version > 1.8) {
            stderr.println("Java versions newer than 1.8 are not supported.");
            System.exit(Integer.MIN_VALUE);
        }

        if (args.length == 0) {
            stderr.println("The first and only required argument must be the path to a Git-repository.");
            System.exit(-1);
        }

        // Disable all kinds of output for code using System.out or System.err:
        Application.toggleSysout(false);

        GitRepository repo = null;
        try {
            repo = new GitRepository(Git.open(new File(args[0])).getRepository(), args[0]);
        } catch (Throwable t) {
            stderr.println("Cannot open Git-repository in: " + args[0]);
            stderr.println(t.getMessage());
            stderr.println(Arrays.stream(t.getStackTrace()).map(s -> s.toString()));
            System.exit(-2);
        }

        // Now let's try to build this..
        MavenService mvnSvc = new MavenService();
        try {
            mvnSvc.run(repo.getAbsolutePath());
        } catch (Throwable t) {
            stderr.println("Cannot build the repo using Maven.");
            stderr.println(t.getMessage());
            System.exit(-3);
        }

        // Now let's try to get the analysis result:
        VizzMetricAnalyzer vizzMetricAnalyzer = new VizzMetricAnalyzer();
        String json = null;
        try {
            Map<MetricName, Metric> commitMetrics = vizzMetricAnalyzer.analyze(repo.getAbsolutePath());
            json = new Gson().toJson(commitMetrics);
        } catch (Throwable t) {
            stderr.println("Cannot obtain metrics using VizzMetricAnalyzer.");
            stderr.println(t.getMessage());
            System.exit(-4);
        }

        // Let's print our JSON to stdout:
        stdout.println(json);
    }
}

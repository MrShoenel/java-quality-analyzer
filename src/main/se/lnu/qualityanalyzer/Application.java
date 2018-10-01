package se.lnu.qualityanalyzer;

import com.google.gson.Gson;
import se.lnu.qualityanalyzer.enums.ExitCodes;
import se.lnu.qualityanalyzer.enums.MetricName;
import se.lnu.qualityanalyzer.model.analysis.Metric;
import se.lnu.qualityanalyzer.service.analysis.impl.VizzMetricAnalyzer;
import se.lnu.qualityanalyzer.service.build.BuildService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

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
            ExitCodes.JAVA_TOO_NEW.exit();
        }

        if (args.length == 0) {
            stderr.println("The first and only required argument must be the path to a Java project that can be built using either Maven or Gradle.");
            stderr.println("Specify --help to get more information.");
            ExitCodes.ARGS.exit();
        }

        if ("--help".equals(args[0])) {
            stderr.println("The only argument supported is a path to a Java project.");
            stderr.println("The possible exit-codes are: " + Arrays.stream(ExitCodes.OK.getDeclaringClass().getEnumConstants()).map(e -> e.toString() + "(" + e.getCode() + ")").collect(Collectors.joining(", ")));
            ExitCodes.OK.exit();
        }

        // Disable all kinds of output for code using System.out or System.err:
        Application.toggleSysout(false);

        File projectDir = new File(args[0]);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            stderr.println("The given directory does not exist or is not a directory.");
            ExitCodes.INVALID_PROJECT.exit();
        }

        // Now let's try to build this..
        try {
            BuildService.build(projectDir);
        } catch (Throwable t) {
            stderr.println("Cannot build the repo: " + t.getMessage());
            t.printStackTrace(stderr);
            ExitCodes.BUILD_ERROR.exit();
        }

        // Now let's try to get the analysis result:
        VizzMetricAnalyzer vizzMetricAnalyzer = new VizzMetricAnalyzer();
        String json = null;
        try {
            Map<MetricName, Metric> commitMetrics = vizzMetricAnalyzer.analyze(projectDir.getAbsolutePath());
            json = new Gson().toJson(commitMetrics);
        } catch (Throwable t) {
            stderr.println("Cannot obtain metrics using VizzMetricAnalyzer: " + t.getMessage());
            t.printStackTrace(stderr);
            ExitCodes.VIZZ_ANALYZER_ERROR.exit();
        }

        // Let's print our JSON to stdout:
        stdout.println(json);
    }
}

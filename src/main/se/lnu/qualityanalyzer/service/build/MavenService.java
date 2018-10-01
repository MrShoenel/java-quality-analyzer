package se.lnu.qualityanalyzer.service.build;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;

public class MavenService implements Service {

    private final static String POM_XML = "pom.xml";
    private final static String DEPENDENCY = "clean dependency:copy-dependencies package -DskipTests";

    public void build(File projectDir) {
        try {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File(projectDir.getAbsolutePath() + File.separator + POM_XML));
            request.setGoals(Collections.singletonList(DEPENDENCY));
            request.setOutputHandler(line -> {});
            request.setErrorHandler(line -> {});

            Invoker invoker = new DefaultInvoker();
            invoker.getLogger().setThreshold(InvokerLogger.FATAL);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            throw new Error(e);
        }
    }
}

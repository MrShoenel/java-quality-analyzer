package se.lnu.qualityanalyzer.service.maven;

import org.apache.maven.shared.invoker.*;
import se.lnu.qualityanalyzer.exception.MavenException;

import java.io.File;
import java.util.Collections;

public class MavenService {

    private final static String POM_XML = "pom.xml";
    private final static String DEPENDENCY = "clean dependency:copy-dependencies package -DskipTests";

    public void run(String sourcePath) throws MavenException {
        try {
            InvocationRequest request = new DefaultInvocationRequest();
            request.setPomFile(new File(sourcePath + File.separator + POM_XML));
            request.setGoals(Collections.singletonList(DEPENDENCY));
            request.setOutputHandler(line -> {});
            request.setErrorHandler(line -> {});

            Invoker invoker = new DefaultInvoker();
            invoker.getLogger().setThreshold(InvokerLogger.FATAL);
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            throw new MavenException(e);
        }
    }
}

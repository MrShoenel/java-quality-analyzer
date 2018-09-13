package se.lnu.qualityanalyzer.exception;

public class MavenException extends Exception {

    public MavenException(String message) {
        super(message);
    }

    public MavenException(String message, Throwable cause) {
        super(message, cause);
    }

    public MavenException(Throwable cause) {
        super(cause);
    }

}

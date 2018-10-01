package se.lnu.qualityanalyzer.enums;

public enum ExitCodes {
    OK(0),
    JAVA_TOO_NEW(Integer.MIN_VALUE),
    ARGS(-1),
    INVALID_PROJECT(-2),
    BUILD_ERROR(-3),
    VIZZ_ANALYZER_ERROR(-4);

    private int code;

    ExitCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    /**
     * Exit (terminate the entire application) using the current code.
     */
    public void exit() {
        System.exit(this.getCode());
    }
}

package se.lnu.qualityanalyzer.enums;

public enum OutputEntityType {
    UPLOAD(0),
    PROJECT(1),
    FILE(2);

    public final int type;

    OutputEntityType(int type) {
        this.type = type;
    }
}

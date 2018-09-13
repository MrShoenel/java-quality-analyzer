package se.lnu.qualityanalyzer.util;

import se.lnu.qualityanalyzer.enums.FileStatus;

public final class GitHubUtil {

    public static FileStatus getFileStatus(String gitHubFileStatus) {
        switch (gitHubFileStatus) {
            case "added" :
                return FileStatus.ADDED;
            case "modified" :
                return FileStatus.MODIFIED;
            case "removed" :
                return FileStatus.REMOVED;
            case "renamed" :
                return FileStatus.MODIFIED;
            default:
                throw new IllegalArgumentException("Invalid status of the committed file: " + gitHubFileStatus);
        }
    }

}

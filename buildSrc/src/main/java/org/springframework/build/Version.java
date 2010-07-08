package org.springframework.build;

import org.gradle.api.tasks.StopExecutionException;


public class Version {

    public enum ReleaseType {
        RELEASE, SNAPSHOT, MILESTONE
    }

    private final String value;
    private final ReleaseType releaseType;

    /**
     * @param dotted-quad version spec, e.g.: 1.0.0.RELEASE
     */
    public Version(String value) {
        this.value = value;
        this.releaseType = releaseTypeFor(value);
    }

    /**
     * @return the dotted-quad version value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @return the ReleaseType associated with this version
     */
    public ReleaseType getReleaseType() {
        return this.releaseType;
    }

    /**
     * @return the version string returned by {@link getValue()}
     */
    public String toString() {
        return this.getValue();
    }

    /**
     * @param dotted-quad version spec, e.g.: 1.0.0.RELEASE
     */
    public static ReleaseType releaseTypeFor(String version) {
        if (version.endsWith("RELEASE")) return ReleaseType.RELEASE;
        if (version.endsWith("SNAPSHOT")) return ReleaseType.SNAPSHOT;
        if (version.matches(".*\\.M[0-9]+$")) return ReleaseType.MILESTONE;

        throw new StopExecutionException("unknown version scheme: " +
            "versions must end in (RELEASE|SNAPSHOT|M[0-9]+), " +
            "but got (" + version + ")");
    }
}

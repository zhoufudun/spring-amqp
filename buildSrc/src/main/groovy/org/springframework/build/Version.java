/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.build;

import org.gradle.api.InvalidUserDataException;

/**
 * A Spring project release version identifier.  Spring project versions can
 * represent either a release, milestone, or snapshot, and have constraints
 * on validity, e.g.: versions must be in dotted-quad format (1.0.0.RELEASE).
 *
 * Such validations are performed herein, and release type is inferred from
 * the version string.
 *
 * @author cbeams
 */
public class Version {

    /**
     * Indicates whether a version is a release, milestone, or snapshot
     */
    public enum ReleaseType {
        RELEASE, MILESTONE, SNAPSHOT
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
     * @return 1.0.x style
     */
    public String getWildcardValue() {
        return "1.0.x";
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

        throw new InvalidUserDataException("unknown version scheme: " +
            "versions must end in (RELEASE|SNAPSHOT|M[0-9]+), " +
            "but got (" + version + ")");
    }
}

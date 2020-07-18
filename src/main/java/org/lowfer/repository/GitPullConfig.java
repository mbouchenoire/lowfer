/*
 * Copyright 2020 the original author or authors from the Lowfer project.
 *
 * This file is part of the Lowfer project, see https://github.com/mbouchenoire/lowfer
 * for more information.
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

package org.lowfer.repository;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Optional;

public final class GitPullConfig {

    private final String uri;
    private final String branch;
    private final GitHostConfig gitHostConfig;
    private final File directory;

    public GitPullConfig(
        String uri,
        @Nullable String branch,
        GitHostConfig gitHostConfig,
        File directory) {

        this.uri = uri;
        this.branch = branch;
        this.gitHostConfig = gitHostConfig;
        this.directory = directory;
    }

    public String getUri() {
        return uri;
    }

    public Optional<String> getBranch() {
        return Optional.ofNullable(branch);
    }

    public GitHostConfig getGitHostConfig() {
        return gitHostConfig;
    }

    public File getDirectory() {
        return directory;
    }
}

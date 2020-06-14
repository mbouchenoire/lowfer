package org.lowfer.repository;

import io.vavr.concurrent.Future;
import org.lowfer.domain.common.Commit;

import java.util.List;

public interface AsyncComponentGitRepository {

    String getComponentName();
    Future<List<Commit>> getCommits(String branchName);
}

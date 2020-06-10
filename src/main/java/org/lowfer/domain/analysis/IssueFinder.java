package org.lowfer.domain.analysis;

import org.lowfer.domain.common.SoftwareArchitecture;

import java.util.List;

public interface IssueFinder {

    List<Issue> find(SoftwareArchitecture architecture);
}

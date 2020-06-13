package org.lowfer.repository;

import org.lowfer.domain.common.SoftwareArchitecture;

import java.util.List;
import java.util.Optional;

public interface ArchitectureRepository {

    Optional<SoftwareArchitecture> findByName(String name);

    List<SoftwareArchitecture> findAll();
}


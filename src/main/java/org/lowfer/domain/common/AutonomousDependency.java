package org.lowfer.domain.common;

public final class AutonomousDependency {

    private final ComponentDependency dependency;
    private final SoftwareComponent source;
    private final SoftwareComponent target;

    public AutonomousDependency(ComponentDependency dependency, SoftwareComponent source, SoftwareComponent target) {
        this.dependency = dependency;
        this.source = source;
        this.target = target;
    }

    public ComponentDependency getDependency() {
        return dependency;
    }

    public SoftwareComponent getSource() {
        return source;
    }

    public SoftwareComponent getTarget() {
        return target;
    }
}

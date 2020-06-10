package org.lowfer.web.rest.vm;

public final class GraphvizView {

    private final String dot;

    public GraphvizView(String dot) {
        this.dot = dot;
    }

    public String getDot() {
        return dot;
    }
}

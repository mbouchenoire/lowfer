package org.lowfer.graphviz;

import org.springframework.stereotype.Component;

@Component
public class GraphvizStyleRepository {

    private static final RoughStyle ROUGH_STYLE = new RoughStyle();
    private static final DefaultStyle DEFAULT_STYLE = new DefaultStyle();

    public GraphvizStyle findByName(String name) {
        if ("rough".equals(name)) {
            return ROUGH_STYLE;
        }

        return DEFAULT_STYLE;
    }
}

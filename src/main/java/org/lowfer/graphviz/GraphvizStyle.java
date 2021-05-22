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

package org.lowfer.graphviz;

import guru.nidi.graphviz.attribute.*;
import org.apache.commons.lang3.StringUtils;
import org.lowfer.config.Color;
import org.lowfer.domain.common.*;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.lowfer.graphviz.GraphType.DATA_FLOW;

public interface GraphvizStyle {

    default String defaultFontFamily() {
        //noinspection SpellCheckingInspection
        return "Lato";
    }

    default String defaultFontColor() {
        return Color.BLACK.getStroke();
    }

    default Attributes<? extends ForGraph> graphDefaultAttrs() {
        return attrs -> attrs
            .add("fontname", defaultFontFamily())
            //.add("bgcolor", "#f9f9f9")
            ;
    }

    default Attributes<? extends ForNode> nodeDefaultAttrs() {
        return Font.name(defaultFontFamily());
    }

    default Attributes<? extends ForLink> linkDefaultAttrs() {
        return attrs -> attrs
            .add("fontname", defaultFontFamily())
            .add("dir", "forward")
            .add("arrowhead", "open");
    }

    default Attributes<? extends ForNode> componentAttrs(SoftwareComponent component) {
        return attrs -> {
            attrs
                .add(labelAndShape(component))
                .add("fontcolor", defaultFontColor());

            return colorize(attrs, component);
        };
    }

    default Attributes<? extends ForLink> dependencyAttrs(
        ComponentDependency dependency, SoftwareArchitecture architecture, GraphType graphType) {

        final MapAttributes<ForLink> attrs = new MapAttributes<>()
            .add("color", Color.GRAY.getStroke())
            .add("fontcolor", defaultFontColor());

        if (dependency.isPublicKey()) {
            return attrs
                .add("label", "pubkey")
                .add("fontsize", 6)
                .add("style", "dashed")
                .add("color", Color.GRAY.getBackground())
                .add("constraint", false)
                .add("penwidth", "0.5");
        } else if (isLibrary(dependency, architecture)) {
            return attrs.add("arrowhead", "diamond");
        } else if (graphType == DATA_FLOW && isConsumer(dependency, architecture)) {
            return attrs;
        } else {
            return attrs;
        }
    }

    private static Attributes<? super ForNode> colorize(
        MapAttributes<? super ForNode> attrs, SoftwareComponent component) {

        final Color color = component.getType().getColor();

        return attrs
            .add("style", "filled")
            .add("fontcolor", color.getStroke())
            .add("color", color.getStroke())
            .add("fillcolor", isEmpty(component.getLabel()) ? color.getBackground() : color.getCanvas())
            ;
    }

    private boolean isConsumer(ComponentDependency dependency, SoftwareArchitecture architecture) {
        return dependency.getDependencyType()
            .filter(dependencyType -> dependencyType == DependencyType.QUEUE_SUBSCRIBE)
            .isPresent();
    }

    private boolean isLibrary(ComponentDependency dependency, SoftwareArchitecture architecture) {
        return architecture.findComponent(dependency).map(SoftwareComponent::getType).filter(t -> t == SoftwareComponentType.LIBRARY).isPresent();
    }

    private MapAttributes<ForNode> labelAndShape(SoftwareComponent component) {
        final var attrs = new MapAttributes<>()
            .add("label", component.getLabel());

        if (StringUtils.isNoneBlank(component.getDescription())) {
            attrs.add("tooltip", component.getDescription());
        }

        switch (component.getType()) {
            case LIBRARY:
                return attrs.add("shape", "folder").add("label", padRectangle(component)).add("color", "blue");
            case SERVICE:
            case STORED_PROCEDURE:
            case FRONTEND:
                return attrs.add("shape", "component").add("label", padRectangle(component));
            case AGGREGATE:
                return attrs.add("shape", "circle");
            case QUEUE:
                return attrs.add("shape", "cds").add("label", padCds(component));
            case CONFIG:
            case SECRET:
                return attrs.add("shape", "note");
            case DATABASE:
                return attrs.add("shape", "cylinder").add("label", "");
            default:
                return attrs.add("shape", "ellipse");
        }
    }

    private static String padRectangle(SoftwareComponent component) {
        final String padding = padding(component.getLabel());
        return padding + component.getLabel() + padding;
    }

    private static String padCds(SoftwareComponent component) {
        final String padding = padding(component.getLabel());
        return padding + component.getLabel() + padding + "    ";
    }

    private static String padding(String label) {
        return " ".repeat(Math.max(0, paddingSize(label)));
    }

    private static int paddingSize(String label) {
        return label.length() / 10 * 3;
    }
}

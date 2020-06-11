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

import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Link;
import guru.nidi.graphviz.model.Node;
import io.vavr.Tuple;
import org.lowfer.domain.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static java.util.stream.Collectors.*;

public final class GraphFactory {

    private static final Logger LOG = LoggerFactory.getLogger(GraphFactory.class);

    public static Graphviz createGraph(
        GraphOptions options, GraphvizStyle style, SoftwareArchitecture architecture) {

        LOG.trace("Creating Graphviz for architecture with {} components ({})...", architecture.getComponents().size(), architecture.getName());

        final Map<SoftwareComponent, Node> nodes = architecture.getComponents().stream()
            .filter(aggregateFilter(options))
            .map(component -> {
                final List<Node> dependencyNodes = component.getDependencies().stream()
                    .flatMap(dependency -> architecture.findComponent(dependency).stream())
                    .filter(aggregateFilter(options))
                    .map(SoftwareComponent::getName)
                    .map(Factory::node)
                    .collect(toList());

                final Node node = node(component.getName())
                    .with(style.componentAttrs(component))
                    .link(dependencyNodes)
                    ;

                return Tuple.of(component, node);
            })
            .collect(Collectors.toMap(tuple -> tuple._1, tuple -> tuple._2));

        final Map<AutonomousDependency, Node> linkedNodes = architecture.getComponents().stream()
            .flatMap(component -> component.getDependencies().stream()
                .map(dependency -> new AutonomousDependency(
                    dependency,
                    component,
                    architecture.findComponentByName(dependency.getComponentName()).orElseThrow())))
            .filter(dependency -> nodes.containsKey(dependency.getSource()) && nodes.containsKey(dependency.getTarget()))
            .filter(dependency -> aggregateFilter(dependency.getSource(), options))
            .filter(dependency -> aggregateFilter(dependency.getTarget(), options))
            .collect(Collectors.toMap(
                dependency -> dependency,
                dependency -> {
                    final Node sourceNode = nodes.get(dependency.getSource());
                    final Node targetNode = nodes.get(dependency.getTarget());

                    final Link link = sourceNode.linkTo(targetNode)
                        .with(style.dependencyAttrs(dependency.getDependency(), architecture, options.getGraphType()));

                    return sourceNode.link(link);
                }));

        final List<String> contexts = nodes.keySet().stream()
            .flatMap(component -> component.getContext().stream())
            .distinct()
            .collect(toUnmodifiableList());

        final List<Graph> subGraphs = contexts.stream()
            .map(context -> {
                final List<Node> nodesForContext = nodes.keySet().stream()
                    .flatMap(component -> component.getContext().stream()
                        .filter(componentContext -> componentContext.equals(context))
                        .map(c -> nodes.get(component)))
                    .collect(toList());

                return graph(context)
                    // if we don't do this, "none" is applied on clusters
                    .linkAttr().with("dir", "forward")
                    .cluster()
                    .with(nodesForContext);
            })
            .collect(toList());

        final List<Node> nodesWithoutContext = nodes.keySet().stream()
            .filter(component -> component.getContext().isEmpty())
            .map(nodes::get)
            .collect(toList());

        final Graph graph = graph(architecture.getName())
            .graphAttr().with(style.graphDefaultAttrs())
            .nodeAttr().with(style.nodeDefaultAttrs())
            .linkAttr().with(style.linkDefaultAttrs())
            .directed()
            .graphAttr().with(Rank.dir(options.getRankDir()))
            .with(linkedNodes.values().stream().collect(toUnmodifiableList()))
            .with(subGraphs)
            .with(nodesWithoutContext)
            .strict();

        LOG.info("Created Graphviz with {} nodes for architecture with {} components", nodes.size(), architecture.getComponents().size());

        return Graphviz.fromGraph(graph);
    }

    private static Predicate<SoftwareComponent> aggregateFilter(GraphOptions options) {
        return targetComponent -> aggregateFilter(targetComponent, options);
    }

    private static boolean aggregateFilter(SoftwareComponent component, GraphOptions options) {
        return component.getType() != SoftwareComponentType.AGGREGATE || !options.isHideAggregates();
    }
}

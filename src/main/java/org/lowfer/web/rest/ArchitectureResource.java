package org.lowfer.web.rest;

import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.lowfer.domain.common.ArchitectureTransformer;
import org.lowfer.domain.common.ComponentFilters;
import org.lowfer.domain.common.SoftwareArchitecture;
import org.lowfer.domain.error.ArchitectureTooBigToRenderException;
import org.lowfer.graphviz.*;
import org.lowfer.service.ArchitectureService;
import org.lowfer.web.rest.vm.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RestController
public class ArchitectureResource {

    private final ArchitectureService architectureService;
    private final ArchitectureTransformer architectureTransformer;
    private final GraphvizStyleRepository graphvizStyleRepository;
    private final int maximumComponentsToRender;

    public ArchitectureResource(
        ArchitectureService architectureService,
        ArchitectureTransformer architectureTransformer,
        GraphvizStyleRepository graphvizStyleRepository,
        @Value("${application.max-component-render:50}") int maximumComponentsToRender) {

        this.architectureService = architectureService;
        this.architectureTransformer = architectureTransformer;
        this.graphvizStyleRepository = graphvizStyleRepository;
        this.maximumComponentsToRender = maximumComponentsToRender;
    }

    @GetMapping("/api/architectures")
    public ArchitectureListView getArchitectures() {
        return architectureService.findAll().stream()
            .map(ArchitectureListItemView::new)
            .sorted(Comparator.comparing(ArchitectureListItemView::getName))
            .collect(collectingAndThen(toList(), ArchitectureListView::new));
    }
    @GetMapping("/api/components")
    public ComponentListView getComponents(
        @RequestParam(name = "architecture-name", required = false, defaultValue = "") String architectureName,
        @RequestParam(name = "architecture-encoded", required = false, defaultValue = "") String architectureEncoded,
        @RequestParam(name = "component-name", required = false, defaultValue = "") String nameFilter,
        @RequestParam(name = "component-type", required = false, defaultValue = "") String typeFilter,
        @RequestParam(name = "maintainer", required = false, defaultValue = "") String maintainerFilter) {

        return architectureService.load(architectureName, architectureEncoded)
            .map(architecture -> filter(architecture, nameFilter, typeFilter, maintainerFilter))
            .map(SoftwareArchitecture::getComponents)
            .map(components -> components.stream()
                .map(ComponentListItemView::new)
                .sorted(Comparator.comparing(ComponentListItemView::getName))
                .collect(toList()))
            .map(ComponentListView::new)
            .get();
    }

    @GetMapping("/api/maintainers")
    public MaintainerListView getMaintainers(
        @RequestParam(name = "architecture-name", required = false, defaultValue = "") String architectureName,
        @RequestParam(name = "architecture-encoded", required = false, defaultValue = "") String architectureEncoded,
        @RequestParam(name = "component-name", required = false, defaultValue = "") String nameFilter,
        @RequestParam(name = "component-type", required = false, defaultValue = "") String typeFilter,
        @RequestParam(name = "maintainer", required = false, defaultValue = "") String maintainerFilter) {

        return architectureService.load(architectureName, architectureEncoded)
            .map(architecture -> filter(architecture, nameFilter, typeFilter, maintainerFilter))
            .map(SoftwareArchitecture::getMaintainers)
            .map(maintainers -> maintainers.stream()
                .map(MaintainerListItemView::new)
                .sorted(Comparator.comparing(MaintainerListItemView::getName))
                .collect(toList()))
            .map(MaintainerListView::new)
            .get();
    }

    @GetMapping("/api/component-types")
    public ComponentTypeListView getComponentTypes(
        @RequestParam(name = "architecture-name", required = false, defaultValue = "") String architectureName,
        @RequestParam(name = "architecture-encoded", required = false, defaultValue = "") String architectureEncoded,
        @RequestParam(name = "component-name", required = false, defaultValue = "") String nameFilter,
        @RequestParam(name = "component-type", required = false, defaultValue = "") String typeFilter,
        @RequestParam(name = "maintainer", required = false, defaultValue = "") String maintainerFilter) {

        return architectureService.load(architectureName, architectureEncoded)
            .map(architecture -> filter(architecture, nameFilter, typeFilter, maintainerFilter))
            .map(SoftwareArchitecture::getComponentTypes)
            .map(componentTypes -> componentTypes.stream().map(ComponentTypeListItemView::new).collect(toList()))
            .map(ComponentTypeListView::new)
            .get();
    }

    private SoftwareArchitecture filter(
        SoftwareArchitecture architecture, String nameFilter, String typeFilter, String maintainerFilter) {

        final ComponentFilters filters = ComponentFilters.builder()
            .name(nameFilter)
            .type(typeFilter)
            .maintainer(maintainerFilter)
            .build();

        return architectureTransformer.filter(architecture, filters);
    }

    @GetMapping("/api/graphviz")
    public GraphvizView downloadGraphviz(
        @RequestParam(name = "architecture-name", required = false, defaultValue = "") String architectureName,
        @RequestParam(name = "architecture-encoded", required = false, defaultValue = "") String architectureEncoded,
        @RequestParam(name = "component-name", required = false, defaultValue = "") String nameFilter,
        @RequestParam(name = "component-type", required = false, defaultValue = "") String typeFilter,
        @RequestParam(name = "internal-only", required = false, defaultValue = "false") boolean internalOnly,
        @RequestParam(name = "maintainer", required = false, defaultValue = "") String maintainerFilter,
        @RequestParam(name = "hide-aggregates", required = false, defaultValue = "false") boolean hideAggregates,
        @RequestParam(name = "style", required = false, defaultValue = "") String style,
        @RequestParam(name = "direction", required = false, defaultValue = "TOP_TO_BOTTOM") Rank.RankDir direction,
        @RequestParam(name = "type", required = false, defaultValue = "DEPENDENCIES") GraphType graphType) {

        final ComponentFilters filters = ComponentFilters.builder()
            .name(nameFilter)
            .type(typeFilter)
            .maintainer(maintainerFilter)
            .internal(internalOnly)
            .build();

        return architectureService.load(architectureName, architectureEncoded)
            .map(architecture -> architectureTransformer.filter(architecture, filters))
            .flatMap(architecture -> architecture.getComponents().size() <= maximumComponentsToRender
                ? success(architecture)
                : failure(new ArchitectureTooBigToRenderException(architecture.getComponents().size())))
            .map(architecture -> {
                final Graphviz graph = GraphFactory.createGraph(
                    new GraphOptions.Builder()
                        .direction(direction)
                        .hideAggregates(hideAggregates)
                        .graphType(graphType)
                        .build(),
                    graphvizStyleRepository.findByName(style),
                    architecture);

                final String dot = graph.render(Format.DOT).toString();
                return DotPostProcessor.process(dot);
            })
            .map(GraphvizView::new)
            .get();
    }

    @GetMapping("/api/issues")
    public List<IssueView> getIssues(
        @RequestParam(name = "architecture-name", required = false, defaultValue = "") String architectureName,
        @RequestParam(name = "architecture-encoded", required = false, defaultValue = "") String architectureEncoded,
        @RequestParam(name = "component-name", required = false, defaultValue = "") String nameFilter,
        @RequestParam(name = "component-type", required = false, defaultValue = "") String typeFilter,
        @RequestParam(name = "maintainer", required = false, defaultValue = "") String maintainerFilter) {

        final ComponentFilters filters = ComponentFilters.builder()
            .name(nameFilter)
            .type(typeFilter)
            .maintainer(maintainerFilter)
            .build();

        return architectureService.load(architectureName, architectureEncoded)
            .map(architecture -> architectureService.findIssues(architecture, filters))
            .get();
    }
}

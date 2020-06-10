package org.lowfer.graphviz;

import guru.nidi.graphviz.attribute.Rank;
import org.hibernate.validator.internal.constraintvalidators.bv.time.past.PastValidatorForReadableInstant;

public final class GraphOptions {

    private final Rank.RankDir rankDir;
    private final String fontName;
    private final int fontSize;
    private final boolean hideAggregates;
    private final GraphType graphType;

    private GraphOptions(
        Rank.RankDir rankDir, String fontName, int fontSize, boolean hideAggregates, GraphType graphType) {

        this.rankDir = rankDir;
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.hideAggregates = hideAggregates;
        this.graphType = graphType;
    }

    Rank.RankDir getRankDir() {
        return rankDir;
    }

    public String getFontName() {
        return fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public boolean isHideAggregates() {
        return hideAggregates;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public static class Builder {

        private Rank.RankDir rankDir = Rank.RankDir.TOP_TO_BOTTOM;
        private String fontName = "Helvetica";
        private int fontSize = 11;
        private boolean hideAggregates = false;
        private GraphType graphType = GraphType.DEPENDENCIES;

        public Builder direction(Rank.RankDir rankDir) {
            this.rankDir = rankDir;
            return this;
        }

        public Builder fontName(String fontName) {
            this.fontName = fontName;
            return this;
        }

        public Builder fontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        public Builder hideAggregates(boolean hideAggregates) {
            this.hideAggregates = hideAggregates;
            return this;
        }

        public Builder graphType(GraphType graphType) {
            this.graphType = graphType;
            return this;
        }

        public GraphOptions build() {
            return new GraphOptions(rankDir, fontName, fontSize, hideAggregates, graphType);
        }
    }
}

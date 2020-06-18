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
        private String fontName = "Lato";
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

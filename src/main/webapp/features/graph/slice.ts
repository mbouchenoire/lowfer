import { createSlice, createSelector, PayloadAction } from '@reduxjs/toolkit';

import { RootState } from '../../store';

export enum GraphDirection {
  TOP_TO_BOTTOM = 'TOP_TO_BOTTOM',
  LEFT_TO_RIGHT = 'LEFT_TO_RIGHT'
}

export enum GraphType {
  DEPENDENCIES = 'DEPENDENCIES',
  DATA_FLOW = 'DATA_FLOW'
}

interface GraphState {
  coarsed: boolean;
  coarsedContent: string;
  direction: GraphDirection;
  hideAggregates: boolean;
  type: GraphType;
  graphRendered: boolean;
}

const initialState: GraphState = {
  coarsed: true,
  direction: GraphDirection.TOP_TO_BOTTOM,
  hideAggregates: false,
  type: GraphType.DEPENDENCIES,
  coarsedContent: '',
  graphRendered: true
};

export const slice = createSlice({
  name: 'graph',
  initialState,
  reducers: {
    toggleCoarsed: (state) => {
      state.coarsed = !state.coarsed;
    },
    toggleHideAggregates: (state) => {
      state.hideAggregates = !state.hideAggregates;
    },
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    renderGraph: (state, action: PayloadAction<string | undefined>) => {
      state.graphRendered = false;
    },
    renderGraphSuccess: (state) => {
      state.graphRendered = true;
    },
    setCoarsedContent: (state, action: PayloadAction<string>) => {
      state.coarsedContent = action.payload;
    },
    setDirection: (state, action: PayloadAction<GraphDirection>) => {
      state.direction = action.payload;
    },
    setType: (state, action: PayloadAction<GraphType>) => {
      state.type = action.payload;
    }
  }
});

export const { actions, reducer } = slice;

const getCoarsed = (state: RootState) => state.graph.coarsed;

const getCoarsedContent = (state: RootState) => state.graph.coarsedContent;

const getGraphDirection = (state: RootState) => state.graph.direction;

const getHideAggregates = (state: RootState) => state.graph.hideAggregates;

const getGraphType = (state: RootState) => state.graph.type;

const getGraphRendered = (state: RootState) => state.graph.graphRendered;

const getStyle = createSelector<RootState, boolean, string>(
  [getCoarsed],
  (coarsed) => (coarsed ? 'rough' : 'default')
);

export const selectors = {
  getCoarsed,
  getCoarsedContent,
  getGraphDirection,
  getHideAggregates,
  getGraphType,
  getGraphRendered,
  getStyle
};

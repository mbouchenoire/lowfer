import { createSlice, PayloadAction, createSelector } from '@reduxjs/toolkit';

import { RootState } from '../../store';

import { equals } from 'ramda';

export enum OverviewStatus {
  IDLE = 'IDLE',
  FETCHING = 'FETCHING',
  ERROR = 'ERROR'
}

interface OverviewState {
  dot: string | null;
  error: string | null;
  status: OverviewStatus;
}

const initialState: OverviewState = {
  dot: null,
  error: null,
  status: OverviewStatus.FETCHING
};

export const slice = createSlice({
  name: 'overview',
  initialState,
  reducers: {
    get: (state) => {
      state.dot = null;
      state.error = null;
      state.status = OverviewStatus.FETCHING;
    },
    getSuccess: (state, action: PayloadAction<string>) => {
      state.dot = action.payload;
      state.error = null;
      state.status = OverviewStatus.IDLE;
    },
    getFailure: (state, action: PayloadAction<string>) => {
      state.error = action.payload;
      state.dot = null;
      state.status = OverviewStatus.ERROR;
    }
  }
});

export const { actions, reducer } = slice;

const getStatus = (state: RootState) => state.overview.status;

const getError = (state: RootState) => state.overview.error;

const getDot = (state: RootState) => state.overview.dot;

const getIsFetching = createSelector(
  [getStatus],
  equals<OverviewStatus>(OverviewStatus.FETCHING)
);

export const selectors = {
  getDot,
  getError,
  getIsFetching,
  getStatus
};

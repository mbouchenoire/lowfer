import { createSelector, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { ArchitectureSource, AppState } from './types';
import { RootState } from '../../store';
import { equals } from 'ramda';

const initialState: AppState = {
  architectureSource: ArchitectureSource.LOCAL
};

export const { actions, reducer } = createSlice({
  name: 'app',
  initialState,
  reducers: {
    setSource: (state, action: PayloadAction<ArchitectureSource>) => {
      state.architectureSource = action.payload;
    }
  }
});

const getArchitectureSource = (state: RootState) =>
  state.app.architectureSource;

const getIsVersionnedArchitecture = createSelector<
  RootState,
  ArchitectureSource | null,
  boolean
>(
  [getArchitectureSource],
  equals<ArchitectureSource | null>(ArchitectureSource.VERSIONNED)
);

export const selectors = {
  getArchitectureSource,
  getIsVersionnedArchitecture
};

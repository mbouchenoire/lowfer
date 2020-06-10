import { createSlice, PayloadAction, createSelector } from '@reduxjs/toolkit';

import { RootState } from '../../store';
import { ArchitectureListItemView } from '../../services';
import { selectors as appSelectors } from '../app/slice';

export enum ArchitecturesStatus {
  IDLE = 'IDLE',
  FETCHING = 'FETCHING',
  ERROR = 'ERROR'
}

interface ArchitecturesState {
  currentIndex: number | null;
  data: ArchitectureListItemView[];
  status: ArchitecturesStatus;
}

const initialState: ArchitecturesState = {
  currentIndex: null,
  data: [],
  status: ArchitecturesStatus.FETCHING
};

export const slice = createSlice({
  name: 'architectures',
  initialState,
  reducers: {
    getList: (state) => {
      state.data = [];
      state.status = ArchitecturesStatus.FETCHING;
    },
    getListSuccess: (
      state,
      action: PayloadAction<ArchitectureListItemView[]>
    ) => {
      state.data = action.payload;
      state.status = ArchitecturesStatus.IDLE;
    },
    getListFailure: (state) => {
      state.data = [];
      state.status = ArchitecturesStatus.ERROR;
    },
    setIndex: (state, action: PayloadAction<number>) => {
      state.currentIndex = action.payload;
    }
  }
});

export const { actions, reducer } = slice;

const getList = (state: RootState) => state.architectures.data;

const getStatus = (state: RootState) => state.architectures.status;

const getCurrentIndex = (state: RootState) => state.architectures.currentIndex;

const getCurrentName = createSelector(
  [getList, getCurrentIndex, appSelectors.getIsVersionnedArchitecture],
  (list, index, isVersionnedArchitecture) => {
    if (!isVersionnedArchitecture) return;
    return index === null ? null : list[index].name;
  }
);

export const selectors = {
  getCurrentIndex,
  getCurrentName,
  getList,
  getStatus
};

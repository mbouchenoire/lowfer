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

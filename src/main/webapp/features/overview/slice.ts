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

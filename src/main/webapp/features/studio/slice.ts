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
import { equals, isNil } from 'ramda';

import { RootState } from '../../store';
import {
  getStateToLocalStorage,
  saveStateToLocalStorage,
  getStudioKeyFromRaw
} from './utils';

import { selectors as appSelectors } from '../app/slice';

export enum StudioStatus {
  IDLE = 'IDLE',
  FETCHING = 'FETCHING',
  ERROR = 'ERROR'
}

export interface StudioDraft {
  dot?: string;
  key: string;
  raw: string;
}

interface StudioState {
  currentIndex: number | null;
  drafts: StudioDraft[];
  error: string | null;
  status: StudioStatus;
}

const drafts = getStateToLocalStorage();

let currentIndex = 0;

const params = new URLSearchParams(window.location.search);
const encodedArchitecture = params.get('encodedArchitecture');

if (encodedArchitecture) {
  drafts.push({ raw: atob(encodedArchitecture), key: '' });
  currentIndex = drafts.length - 1;
}

const initialState: StudioState = {
  currentIndex,
  drafts,
  error: null,
  status: StudioStatus.IDLE
};

export const slice = createSlice({
  name: 'studio',
  initialState,
  reducers: {
    setIndex: (state, action: PayloadAction<number | null>) => {
      state.currentIndex = action.payload;
      if (state.currentIndex === null) return;
      state.drafts[state.currentIndex].dot = '';
      state.error = null;
    },
    setDot: (state, action: PayloadAction<string>) => {
      if (state.currentIndex === null) return;
      state.status = StudioStatus.IDLE;
      state.drafts[state.currentIndex].dot = action.payload;
      state.error = null;
    },
    setError: (state, action: PayloadAction<string>) => {
      if (state.currentIndex === null) return;
      state.status = StudioStatus.ERROR;
      state.error = action.payload;
    },
    setRaw: (state, action: PayloadAction<string>) => {
      if (state.currentIndex === null) return;
      state.status = StudioStatus.FETCHING;
      state.drafts[state.currentIndex].raw = action.payload;
      const currentDraftKey = getStudioKeyFromRaw(action.payload);
      if (currentDraftKey) {
        state.drafts[state.currentIndex].key = currentDraftKey;
      }
      saveStateToLocalStorage(state.drafts);
      state.error = null;
    },
    setName: (state, action: PayloadAction<string>) => {
      if (state.currentIndex === null) return;
      state.drafts[state.currentIndex].key = action.payload;
      saveStateToLocalStorage(state.drafts);
    }
  }
});

export const { actions, reducer } = slice;

const getDrafts = (state: RootState) => state.studio.drafts;

const getStatus = (state: RootState) => state.studio.status;

const getIsFetching = createSelector<RootState, StudioStatus, boolean>(
  [getStatus],
  equals<StudioStatus>(StudioStatus.FETCHING)
);

const getCurrentIndex = (state: RootState) => state.studio.currentIndex;

const getCurrentDot = createSelector(
  [getDrafts, getCurrentIndex],
  (drafts: StudioDraft[], currentIndex: number | null) =>
    currentIndex !== null ? drafts[currentIndex].dot : null
);

const getCurrentRaw = createSelector(
  [getDrafts, getCurrentIndex],
  (drafts, current) => (isNil(current) ? '' : drafts[current].raw)
);

const getCurrentEncodedArchitecture = createSelector(
  [getCurrentRaw, appSelectors.getIsVersionnedArchitecture],
  (raw, isVersionnedArchitecture) =>
    isVersionnedArchitecture || raw === '' ? undefined : btoa(raw)
);

const getCurrentName = createSelector(
  [getDrafts, getCurrentIndex, appSelectors.getIsVersionnedArchitecture],
  (drafts, index, isVersionnedArchitecture) => {
    if (isVersionnedArchitecture) return;
    return isNil(index) ? '' : drafts[index].key;
  }
);

const getError = (state: RootState) => state.studio.error;

export const selectors = {
  getCurrentEncodedArchitecture,
  getCurrentDot,
  getCurrentIndex,
  getCurrentName,
  getCurrentRaw,
  getDrafts,
  getError,
  getIsFetching,
  getStatus
};

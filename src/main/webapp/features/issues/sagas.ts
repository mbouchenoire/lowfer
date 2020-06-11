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

import { all, call, put, select, takeLatest } from 'redux-saga/effects';

import { actions, selectors } from './slice';
import { actions as appActions } from '../app/slice';
import {
  actions as graphActions,
  selectors as graphSelectors
} from '../graph/slice';
import { selectors as architecturesSelectors } from '../architectures/slice';
import {
  actions as studioActions,
  selectors as studioSelectors
} from '../studio/slice';
import {
  actions as filtersActions,
  selectors as filtersSelectors
} from '../filters/slice';
import { architectureResourceApi, basePath } from '../../services/config';

function* removeCurrentIssue() {
  yield put(actions.setActiveIssueId(null));
}

function* get() {
  yield call(removeCurrentIssue);
  const encodedArchitecture = yield select(
    studioSelectors.getCurrentEncodedArchitecture
  );
  const architectureName = yield select(architecturesSelectors.getCurrentName);
  if (!architectureName && !encodedArchitecture) return;
  const { components, componentTypes, maintainers } = yield select(
    filtersSelectors.getFilters
  );
  const request = architectureResourceApi.getIssuesUsingGET(
    encodedArchitecture,
    architectureName,
    components.join(', '),
    componentTypes.join(', '),
    maintainers.join(', ')
  );
  try {
    const { data } = yield call(request, undefined, basePath);
    yield put(
      actions.getSuccess(
        data.map((d: any, index: number) => ({ ...d, id: index }))
      )
    );
  } catch (err) {
    const errorDetail = err?.response?.data?.detail;
    yield put(actions.getFailure(errorDetail));
  }
}

function* getActiveIssue() {
  const style = yield select(graphSelectors.getStyle);
  const direction = yield select(graphSelectors.getGraphDirection);
  const hideAggregates = yield select(graphSelectors.getHideAggregates);
  const data = yield select(selectors.getActiveIssueData);
  if (!data?.encodedArchitecture?.encoded) return;
  const request = architectureResourceApi.downloadGraphvizUsingGET(
    data.encodedArchitecture.encoded,
    undefined,
    undefined,
    undefined,
    direction,
    hideAggregates,
    undefined,
    undefined,
    style
  );
  try {
    const { data } = yield call(request, undefined, basePath);
    yield put(actions.getActiveIssueSuccess(data.dot));
  } catch (err) {
    const errorDetail = err?.response?.data?.detail;
    yield put(actions.getActiveIssueFailure(errorDetail));
  }
}

function* setFilters() {
  yield put(actions.setActiveIssueId(null));
  yield call(get);
}

export default function* root() {
  yield all([
    takeLatest(filtersActions.setFilters, setFilters),
    takeLatest(actions.get, get),
    takeLatest(actions.setActiveIssueId, getActiveIssue),
    takeLatest(actions.getActiveIssue, getActiveIssue),
    takeLatest(graphActions.setDirection, getActiveIssue),
    takeLatest(graphActions.setType, getActiveIssue),
    takeLatest(graphActions.toggleHideAggregates, getActiveIssue),
    takeLatest(studioActions.setDot, get),
    takeLatest(appActions.setSource, get)
  ]);
}

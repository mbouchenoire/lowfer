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

import { actions } from './slice';
import { actions as appActions } from '../app/slice';
import { selectors as architecturesSelectors } from '../architectures/slice';
import { selectors as studioSelectors } from '../studio/slice';
import {
  actions as filtersActions,
  selectors as filtersSelectors
} from '../filters/slice';

import {
  actions as graphActions,
  selectors as graphSelectors
} from '../graph/slice';
import { architectureResourceApi, basePath } from '../../services/config';

function* get() {
  const { components, componentTypes, maintainers } = yield select(
    filtersSelectors.getFilters
  );
  const style = yield select(graphSelectors.getStyle);
  const direction = yield select(graphSelectors.getGraphDirection);
  const hideAggregates = yield select(graphSelectors.getHideAggregates);
  const type = yield select(graphSelectors.getGraphType);
  const encodedArchitecture = yield select(
    studioSelectors.getCurrentEncodedArchitecture
  );
  const architectureName = yield select(architecturesSelectors.getCurrentName);
  if (!architectureName && !encodedArchitecture) return;
  const request = architectureResourceApi.downloadGraphvizUsingGET(
    encodedArchitecture,
    architectureName,
    components.join(','),
    componentTypes.join(','),
    direction,
    hideAggregates,
    undefined,
    maintainers.join(','),
    style,
    type
  );
  try {
    const { data } = yield call(request, undefined, basePath);
    yield put(actions.getSuccess(data.dot));
  } catch (err) {
    const errorDetail = err?.response?.data?.detail;
    yield put(actions.getFailure(errorDetail));
  }
}

export default function* root() {
  yield all([
    takeLatest(actions.get, get),
    takeLatest(appActions.setSource, get),
    takeLatest(filtersActions.addFilters, get),
    takeLatest(filtersActions.setFilters, get),
    takeLatest(graphActions.setDirection, get),
    takeLatest(graphActions.setType, get),
    takeLatest(graphActions.toggleCoarsed, get),
    takeLatest(graphActions.toggleHideAggregates, get),
    takeLatest(graphActions.setType, get)
  ]);
}

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
import {
  actions as graphActions,
  selectors as graphSelectors
} from '../graph/slice';
import { architectureResourceApi, basePath } from '../../services/config';
import { RoutePath } from '../../routes';

function* downloadGraphviz() {
  if (window.location.pathname !== RoutePath.DRAFT) return;
  const encodedArchitecture = yield select(
    selectors.getCurrentEncodedArchitecture
  );

  const direction = yield select(graphSelectors.getGraphDirection);
  const hideAggregates = yield select(graphSelectors.getHideAggregates);
  const style = yield select(graphSelectors.getStyle);
  const type = yield select(graphSelectors.getGraphType);

  const request = architectureResourceApi.downloadGraphvizUsingGET(
    encodedArchitecture,
    undefined,
    undefined,
    undefined,
    direction,
    hideAggregates,
    undefined,
    undefined,
    style,
    type
  );
  try {
    const { data } = yield call(request, undefined, basePath);
    yield put(actions.setDot(data.dot));
  } catch (err) {
    const errorDetail = err?.response?.data?.detail;
    yield put(actions.setError(errorDetail));
  }
}

function* getComponentTypes() {
  const request = architectureResourceApi.getComponentTypesUsingGET();
  try {
    const data = yield call(request);
    console.log({ data });
  } catch (err) {
    console.log({ err });
  }
}

export default function* root() {
  yield all([
    takeLatest(actions.setRaw, downloadGraphviz),
    takeLatest(actions.getComponentTypes, getComponentTypes),
    takeLatest(graphActions.setType, downloadGraphviz),
    takeLatest(graphActions.setDirection, downloadGraphviz),
    takeLatest(graphActions.toggleHideAggregates, downloadGraphviz),
    takeLatest(graphActions.setType, downloadGraphviz)
  ]);
}

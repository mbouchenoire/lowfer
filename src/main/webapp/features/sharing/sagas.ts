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

import { all, select, takeLatest } from 'redux-saga/effects';

import actions from './actions';
import { selectors as architecturesSelectors } from '../architectures/slice';
import { selectors as filtersSelectors } from '../filters/slice';
import { selectors as studioSelectors } from '../studio/slice';
import { copyToClipboard } from '../studio/utils';

function* share() {
  const architectureName = yield select(architecturesSelectors.getCurrentName);
  const encodedArchitecture = yield select(
    studioSelectors.getCurrentEncodedArchitecture
  );
  const filters = yield select(filtersSelectors.getFilters);
  const url = new URL(window.location.href);
  const params = new URLSearchParams();

  if (encodedArchitecture) {
    params.set('encodedArchitecture', encodedArchitecture);
  }
  if (architectureName) {
    params.set('architectureName', architectureName);
  }
  if (filters.components.length) {
    params.set('components', filters.components);
  }
  if (filters.maintainers.length) {
    params.set('maintainers', filters.maintainers);
  }
  if (filters.componentTypes.length) {
    params.set('componentTypes', filters.componentTypes);
  }
  url.search = params.toString();
  copyToClipboard(url.toString());
}

export default function* root() {
  yield all([takeLatest(actions.share.type, share)]);
}

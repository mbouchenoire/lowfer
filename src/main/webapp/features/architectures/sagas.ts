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

import { all, call, put, takeLatest } from 'redux-saga/effects';

import { actions } from './slice';
import { architectureResourceApi, basePath } from '../../services/config';
import { ArchitectureListItemView, ArchitectureListView } from '../../services';

function* get() {
  const params = new URLSearchParams(window.location.search);
  let architectureName = params.get('architectureName');

  const request = architectureResourceApi.getArchitecturesUsingGET();
  try {
    const { data }: { data: ArchitectureListView } = yield call(
      request,
      undefined,
      basePath
    );
    if (!data.architectures) return;
    yield put(actions.getListSuccess(data.architectures));
    if (architectureName) {
      const index = data.architectures.findIndex(
        (architecture: ArchitectureListItemView) =>
          architecture.name === architectureName
      );
      if (index !== -1) yield put(actions.setIndex(index));
      architectureName = null;
    }
  } catch (err) {
    yield put(actions.getListFailure());
  }
}

export default function* root() {
  yield all([takeLatest(actions.getList, get)]);
}

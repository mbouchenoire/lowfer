import { all, call, put, takeLatest } from 'redux-saga/effects';

import { actions } from './slice';
import { architectureResourceApi, basePath } from '../../services/config';
import { ArchitectureListItemView } from '../../services';

function* get() {
  const params = new URLSearchParams(window.location.search);
  let architectureName = params.get('architectureName');

  const request = architectureResourceApi.getArchitecturesUsingGET();
  try {
    const { data } = yield call(request, undefined, basePath);
    yield put(actions.getListSuccess(data.architectures));
    if (architectureName) {
      const index =
        data.architectures.findIndex(
          (architecture: ArchitectureListItemView) =>
            architecture.name === architectureName
        ) || null;
      yield put(actions.setIndex(index));
      architectureName = null;
    }
  } catch (err) {
    yield put(actions.getListFailure());
  }
}

export default function* root() {
  yield all([takeLatest(actions.getList, get)]);
}

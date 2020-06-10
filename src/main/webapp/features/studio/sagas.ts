import { all, call, put, select, takeLatest } from 'redux-saga/effects';

import { actions, selectors } from './slice';
import {
  actions as graphActions,
  selectors as graphSelectors
} from '../graph/slice';
import { architectureResourceApi, basePath } from '../../services/config';

function* downloadGraphviz() {
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

export default function* root() {
  yield all([
    takeLatest(actions.setRaw, downloadGraphviz),
    takeLatest(graphActions.setType, downloadGraphviz),
    takeLatest(graphActions.setDirection, downloadGraphviz),
    takeLatest(graphActions.toggleHideAggregates, downloadGraphviz),
    takeLatest(graphActions.setType, downloadGraphviz)
  ]);
}

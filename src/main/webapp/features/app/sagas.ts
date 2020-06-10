import { all, call, put, takeLatest } from 'redux-saga/effects';

import { actions } from './slice';
import { actions as architecturesActions } from '../architectures/slice';
import { actions as studioActions } from '../studio/slice';
import { ArchitectureSource } from './types';

function* setSource(source: ArchitectureSource) {
  yield put(actions.setSource(source));
}

function* setArchitectureIndex() {
  yield call(setSource, ArchitectureSource.VERSIONNED);
}

function* setStudioIndex() {
  yield call(setSource, ArchitectureSource.LOCAL);
}

export default function* root() {
  yield all([
    takeLatest(architecturesActions.setIndex, setArchitectureIndex),
    takeLatest(studioActions.setIndex, setStudioIndex)
  ]);
}

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

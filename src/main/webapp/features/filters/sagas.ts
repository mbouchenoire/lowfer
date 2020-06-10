import { all, call, put, select, takeLatest } from 'redux-saga/effects';

import { actions } from './slice';
import { actions as appActions } from '../app/slice';
import { selectors as studioSelectors } from '../studio/slice';

import { architectureResourceApi, basePath } from '../../services/config';
import { FilterPayload } from './slice';
import { selectors as architecturesSelectors } from '../architectures/slice';

function* getComponentTypes(
  encodedArchitecture?: string,
  architectureName?: string,
  componentType?: string,
  maintainer?: string
) {
  const request = architectureResourceApi.getComponentTypesUsingGET(
    encodedArchitecture,
    architectureName,
    componentType,
    maintainer
  );
  const { data } = yield call(request, undefined, basePath);
  return data.componentTypes;
}

function* getComponents(
  encodedArchitecture?: string,
  architectureName?: string,
  componentType?: string,
  maintainer?: string
) {
  const request = architectureResourceApi.getComponentsUsingGET(
    encodedArchitecture,
    architectureName,
    componentType,
    maintainer
  );
  const { data } = yield call(request, undefined, basePath);
  return data.components;
}

function* getMaintainers(
  encodedArchitecture: string,
  architectureName?: string,
  componentType?: string,
  maintainer?: string
) {
  const request = architectureResourceApi.getMaintainersUsingGET(
    encodedArchitecture,
    architectureName,
    componentType,
    maintainer
  );
  const { data } = yield call(request, undefined, basePath);
  return data.maintainers;
}

function* getInitial() {
  const encodedArchitecture = yield select(
    studioSelectors.getCurrentEncodedArchitecture
  );
  const architectureName = yield select(architecturesSelectors.getCurrentName);
  const params = new URLSearchParams(window.location.search);
  const components = params.get('components');

  const componentTypes = params.get('componentTypes');

  const maintainers = params.get('maintainers');

  try {
    const data: FilterPayload = yield all({
      components: call(getComponents, encodedArchitecture, architectureName),
      componentTypes: call(
        getComponentTypes,
        encodedArchitecture,
        architectureName
      ),
      maintainers: call(getMaintainers, encodedArchitecture, architectureName)
    });
    yield put(actions.getInitialSuccess(data));
    if (components)
      yield put(
        actions.setFilters({ name: 'components', value: components.split(',') })
      );
    if (componentTypes)
      yield put(
        actions.setFilters({
          name: 'componentTypes',
          value: componentTypes.split(',')
        })
      );
    if (maintainers)
      yield put(
        actions.setFilters({
          name: 'maintainers',
          value: maintainers.split(',')
        })
      );
    window.history.replaceState({}, '', window.location.pathname);
  } catch (err) {
    const errorDetail = err?.response?.data?.detail;
    yield put(actions.getInitialFailure(errorDetail));
  }
}

function* refresh() {
  yield put(actions.resetFilters());
  yield put(actions.getInitial());
}

export default function* root() {
  yield all([
    takeLatest(actions.getInitial, getInitial),
    takeLatest(appActions.setSource, refresh)
  ]);
}

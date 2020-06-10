import {
  configureStore,
  ThunkAction,
  Action,
  combineReducers
} from '@reduxjs/toolkit';
import createSagaMiddleware from 'redux-saga';
import { all, fork } from 'redux-saga/effects';
import { persistStore, persistReducer } from 'redux-persist';
import storage from 'redux-persist/lib/storage';

import { reducer as app } from './features/app/slice';
import { reducer as architectures } from './features/architectures/slice';
import { reducer as filters } from './features/filters/slice';
import { reducer as graph } from './features/graph/slice';
import { reducer as issues } from './features/issues/slice';
import { reducer as overview } from './features/overview/slice';
import { reducer as studio } from './features/studio/slice';
import appSagas from './features/app/sagas';
import architecturesSagas from './features/architectures/sagas';
import filtersSagas from './features/filters/sagas';
import graphSagas from './features/graph/sagas';
import issuesSagas from './features/issues/sagas';
import overviewSagas from './features/overview/sagas';
import sharingSagas from './features/sharing/sagas';
import studioSagas from './features/studio/sagas';

const sagaMiddleware = createSagaMiddleware();

const persistConfig = {
  key: 'root-0.0.4',
  storage,
  whitelist: []
};

const reducer = persistReducer(
  persistConfig,
  combineReducers({
    app,
    architectures,
    filters,
    graph,
    issues,
    overview,
    studio
  })
);

export const store = configureStore({
  reducer,
  middleware: [sagaMiddleware]
});

export const persistor = persistStore(store);

function* rootSaga() {
  yield all([
    fork(architecturesSagas),
    fork(filtersSagas),
    fork(graphSagas),
    fork(issuesSagas),
    fork(overviewSagas),
    fork(sharingSagas),
    fork(studioSagas),
    fork(appSagas)
  ]);
}

sagaMiddleware.run(rootSaga);

export type RootState = ReturnType<typeof store.getState>;
export type AppThunk = ThunkAction<void, RootState, unknown, Action<string>>;

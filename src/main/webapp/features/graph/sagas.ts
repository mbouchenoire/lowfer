import { all, call, put, select, takeLatest } from 'redux-saga/effects';

import { actions, selectors } from './slice';
import { coarse } from './utils';
import { mutationConfig, mutationObserver } from './observer';
import { PayloadAction } from '@reduxjs/toolkit';

const graphvizWork = (d3Elm: any, dot: string) =>
  new Promise((resolve) => {
    d3Elm
      // @ts-ignore
      .graphviz({
        convertEqualSidedPolygons: false,
        tweenPaths: false,
        tweenShapes: false,
        useWorker: true
      })
      .addImage('/svg/database.svg', '100px', '100px')
      .addImage('/svg/frontend.svg', '100px', '100px')
      .renderDot(dot)
      .transition(() =>
        window.d3.transition('main').ease(window.d3.easeSinInOut).duration(200)
      )
      .on('end', () => {
        resolve();
      });
  });

function* renderGraph(action: PayloadAction<string | undefined>) {
  yield put(actions.setCoarsedContent(''));
  const { payload: dot } = action;
  if (!dot) return;
  const graphDom = document.getElementById('#graph');
  if (!graphDom) return;
  const d3Elm = window.d3.select(graphDom);
  if (!d3Elm) return;
  yield call(graphvizWork, d3Elm, dot);
  const coarsed = yield select(selectors.getCoarsed);
  if (!coarsed) {
    mutationObserver.disconnect();
    yield put(actions.renderGraphSuccess());
    return;
  }
  const svgElm = graphDom.querySelector('svg');
  const coarsedContent = coarse(svgElm);

  mutationObserver.observe(svgElm as Node, mutationConfig);
  yield put(actions.setCoarsedContent(coarsedContent));
  yield put(actions.renderGraphSuccess());
}

export default function* () {
  yield all([takeLatest(actions.renderGraph, renderGraph)]);
}

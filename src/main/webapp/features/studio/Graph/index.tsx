import React from 'react';

import { useSelector } from 'react-redux';

import { RootState } from '../../../store';
import { selectors } from '../slice';

import GraphComponent from '../../graph/Graph';

import './styles.scss';

const Graph = () => {
  const dot = useSelector(selectors.getCurrentDot);
  const isFetching = useSelector<RootState, boolean>(selectors.getIsFetching);
  return (
    <div className="GraphContainer">
      <GraphComponent dot={dot ?? ''} isFetching={isFetching} />
    </div>
  );
};

export default Graph;

import React, { useEffect } from 'react';
import { Message } from 'semantic-ui-react';
import { useDispatch, useSelector } from 'react-redux';

import { actions, selectors } from './slice';
import { selectors as architecturesSelectors } from '../architectures/slice';
import { selectors as studioSelectors } from '../studio/slice';
import Error from '../../components/Error';
import Filters from '../filters/Filters';
import Graph from '../graph/Graph';

import './styles.scss';

const Overview = () => {
  const dispatch = useDispatch();
  const dot = useSelector(selectors.getDot) ?? '';
  const error = useSelector(selectors.getError);
  const isFetching = useSelector(selectors.getIsFetching);
  const currentDraftIndex = useSelector(studioSelectors.getCurrentIndex);
  const currentArchitectureName = useSelector(
    architecturesSelectors.getCurrentName
  );

  useEffect(() => {
    dispatch(actions.get());
  }, [dispatch]);

  return (
    <div className="Overview">
      {currentArchitectureName === null && currentDraftIndex === null ? (
        <Message
          className="NoDraft"
          icon="info"
          info
          header="No architecture selected"
          content="Please select an architecture"
        />
      ) : (
        <>
          <Filters />
          <div className="Overview-container">
            <div className="Overview-graphContainer">
              {error !== null ? (
                <Error error={error} />
              ) : (
                <Graph isFetching={isFetching} dot={dot} />
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default Overview;

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

import React, { useCallback, useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Button, Message } from 'semantic-ui-react';
import Error from '../../components/Error';
import { selectors as architecturesSelectors } from '../architectures/slice';
import Filters from '../filters/Filters';
import Graph from '../graph/Graph';
import { selectors as studioSelectors } from '../studio/slice';
import { actions, selectors } from './slice';
import './styles.scss';

const Overview = () => {
  const [sidebarVisibility, setSidebarVisibility] = useState(false);
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

  const toggleSidebarVisibility = useCallback(() => {
    setSidebarVisibility(!sidebarVisibility);
  }, [sidebarVisibility]);

  return (
    <div className="ui bottom attached segment pushable">
      {sidebarVisibility ? (
        <div className="ui left vertical sidebar menu visible">
          <Filters />
        </div>
      ) : (
        <div className="ui left vertical sidebar menu">
          <Filters />
        </div>
      )}

      <div className="pusher Overview">
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
            <div className="Overview-container">
              <div className="Overview-graphContainer">
                {error !== null ? (
                  <Error error={error} />
                ) : (
                  <Graph isFetching={isFetching} dot={dot} />
                )}
              </div>
            </div>
            <Button
              className="Overlay"
              variant="secondary"
              onClick={toggleSidebarVisibility}
            >
              {sidebarVisibility ? 'Hide filters' : 'Show Filters'}
            </Button>
          </>
        )}
      </div>
    </div>
  );
};

export default Overview;

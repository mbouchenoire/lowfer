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

import React, { useEffect } from 'react';
import { Loader } from 'semantic-ui-react';
import { useSelector, useDispatch } from 'react-redux';
import classnames from 'classnames';

import { actions, selectors } from './slice';

import './styles.scss';
import Settings from './Settings';

type Props = {
  dot?: string;
  isFetching: boolean;
};

const Graph = ({ dot, isFetching }: Props) => {
  const coarsed = useSelector(selectors.getCoarsed);
  const coarsedContent = useSelector(selectors.getCoarsedContent);
  const dispatch = useDispatch();
  const graphRendered = useSelector(selectors.getGraphRendered);

  useEffect(() => {
    if (!dot || dot === '') return;
    dispatch(actions.renderGraph(dot));
  }, [dot]);

  return (
    <div className={classnames('Graph', { coarsed })}>
      <Settings dot={dot} />
      <div
        className={classnames('Graph-clean', {
          hidden: isFetching || !graphRendered,
          coarsed
        })}
        id="#graph"
      />
      <Loader active={!graphRendered || isFetching} size="huge" />
      {coarsedContent !== '' && (
        <div
          className="Graph-coarsed"
          dangerouslySetInnerHTML={{ __html: coarsedContent }}
        />
      )}
    </div>
  );
};

export default Graph;

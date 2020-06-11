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

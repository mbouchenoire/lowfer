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
import { isNil } from 'ramda';
import { useSelector, useDispatch } from 'react-redux';
import { Message } from 'semantic-ui-react';

import { actions as filtersActions } from '../../filters/slice';
import { selectors } from '../slice';
import Editor from '../Editor';
import Error from '../../../components/Error';
import Graph from '../Graph';

import './styles.scss';

const Studio = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(filtersActions.resetFilters());
  }, []);

  const currentStudioIndex = useSelector(selectors.getCurrentIndex);

  const error = useSelector(selectors.getError);

  console.log({ currentStudioIndex });

  if (isNil(currentStudioIndex)) {
    return (
      <Message
        className="NoDraft"
        icon="info"
        info
        header="No draft selected"
        content="Please select a draft"
      />
    );
  }

  return (
    <>
      <Editor />
      <div className="Studio-graph">
        {error !== null ? <Error error={error} /> : <Graph />}
      </div>
    </>
  );
};

export default Studio;

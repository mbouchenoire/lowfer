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

import React, { useCallback } from 'react';
import { isNil } from 'ramda';
import { useDispatch, useSelector } from 'react-redux';
import { Loader, Message, Item } from 'semantic-ui-react';

import { actions, selectors, IssueType, IssuesStatus } from './slice';
import { selectors as studioSelectors } from '../studio/slice';
import { selectors as architecturesSelectors } from '../architectures/slice';
import ActiveIssue from './ActiveIssue';
import Error from '../../components/Error';
import Issue from './Issue';
import NatureSvgSrc from './assets/nature.svg';

import './style.scss';
import Filters from '../filters/Filters';

const Issues = () => {
  const activeIssueId = useSelector(selectors.getActiveIssueId);
  const currentDraftIndex = useSelector(studioSelectors.getCurrentIndex);
  const dispatch = useDispatch();
  const issueKeys = useSelector(selectors.getByKey);
  const error = useSelector(selectors.getError);
  const issues = useSelector(selectors.get);
  const status = useSelector(selectors.getStatus);
  const currentArchitectureName = useSelector(
    architecturesSelectors.getCurrentName
  );

  const setActiveIssueId = useCallback(
    (id) => {
      dispatch(actions.setActiveIssueId(id));
    },
    [dispatch]
  );

  if (currentArchitectureName === null && currentDraftIndex === null)
    return (
      <Message
        className="NoDraft"
        icon="info"
        info
        header="No architecture selected"
        content="Please select an architecture"
      />
    );
  if (status === IssuesStatus.FETCHING) return <Loader active size="huge" />;
  if (status === IssuesStatus.ERROR)
    return (
      <div className="Issues">
        <Error error={error} />
      </div>
    );
  if (issues.length === 0)
    return (
      <>
        <Filters />
        <div className="Issues">
          <Message
            icon="check"
            success
            header="Congratulations"
            content="We haven't found any issue in your architecture"
          />
          <img src={NatureSvgSrc} />
        </div>
      </>
    );
  return (
    <>
      <div className="Issues Issues--withList">
        <Filters />
        <div className="Issues-list">
          <Item.Group divided>
            {Object.keys(issueKeys).map((key) => {
              // @ts-ignore
              const issueCategory: IssueType[] = issueKeys[key];
              return issueCategory.map((issue: IssueType) => (
                <Issue
                  {...issue}
                  active={activeIssueId === issue.id}
                  key={String(issue.id)}
                  onClick={() => setActiveIssueId(issue.id)}
                />
              ));
            })}
          </Item.Group>
        </div>
        {!isNil(activeIssueId) ? (
          <ActiveIssue />
        ) : (
          <div className="Issue-active Issue-active--noIssueSelected">
            <Message
              icon="info"
              info
              header="No issue selected"
              content="Select an issue to expand its details"
            />
          </div>
        )}
      </div>
    </>
  );
};

export default Issues;

import React, { Suspense, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { actions, selectors, IssuesStatus } from './slice';
import contents from './contents';
import Error from '../../components/Error';
import Graph from '../graph/Graph';
import { Segment, Label, Icon } from 'semantic-ui-react';
import { severityColor } from './utils';

const ActiveIssue = () => {
  const activeIssue = useSelector(selectors.getActiveIssue);
  const activeIssueData = useSelector(selectors.getActiveIssueData);
  const activeIssueError = useSelector(selectors.getActiveIssueError);
  const rule = activeIssueData?.rule;
  const severity = activeIssueData?.severity;
  const type = activeIssueData?.type;
  const summary = activeIssueData?.summary;
  const description = activeIssueData?.description;
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(actions.getActiveIssue());
  }, [dispatch]);

  const Content =
    // @ts-ignore
    (rule && contents[rule.name]) || contents.APPLY_STABLE_DEPENDENCY_PRINCIPLE;

  return (
    <div className="Issue-active">
      <Segment color={severityColor(severity)}>
        <h2>{summary}</h2>
        <p>
          <Label>
            <Icon name="warning circle" color={severityColor(severity)} />
            {severity}
          </Label>
          <Label>
            <Icon name="first aid" />
            {type}
          </Label>
        </p>
        <p>{description}</p>
        {activeIssue.status === IssuesStatus.ERROR ? (
          <Error error={activeIssueError} />
        ) : (
          <div className="Issue-active-graphContainer">
            <Graph
              dot={activeIssue.data}
              isFetching={activeIssue.status === IssuesStatus.FETCHING}
            />
          </div>
        )}
        <div className="Issue-active-content">
          <Suspense fallback={null}>
            <Content />
          </Suspense>
        </div>
      </Segment>
    </div>
  );
};

export default ActiveIssue;

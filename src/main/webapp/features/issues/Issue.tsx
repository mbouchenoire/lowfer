import React from 'react';
import { Icon, Label, Card } from 'semantic-ui-react';

import { IssueView, IssueViewSeverityEnum } from '../../services';
import { severityColor } from './utils';

const SeverityIcon = ({ severity }: { severity?: IssueViewSeverityEnum }) => {
  switch (severity) {
    default:
    case IssueViewSeverityEnum.CRITICAL:
      return <Icon name="warning sign" color={severityColor(severity)} />;
    case IssueViewSeverityEnum.BLOCKER:
      return <Icon name="warning circle" color={severityColor(severity)} />;
    case IssueViewSeverityEnum.MAJOR:
      return <Icon name="warning" color={severityColor(severity)} />;
    case IssueViewSeverityEnum.MINOR:
      return <Icon name="info" color={severityColor(severity)} />;
  }
};

type Props = IssueView & {
  active: boolean;
  onClick: Function;
};

const Issue = ({ active, onClick, rule, severity, summary, type }: Props) => (
  <Card
    className={active ? 'Issue-selected' : ''}
    color={active ? severityColor(severity) : undefined}
    fluid
    link
    // @ts-ignore
    onClick={onClick}
  >
    <Card.Content>
      <Card.Header>{summary}</Card.Header>
      <Card.Meta>
        <span>{rule?.label}</span>
      </Card.Meta>
    </Card.Content>
    <Card.Content extra>
      <Label>
        <SeverityIcon severity={severity} />
        {severity}
      </Label>
      <Label>
        <Icon name="first aid" />
        {type}
      </Label>
    </Card.Content>
  </Card>
);
export default Issue;

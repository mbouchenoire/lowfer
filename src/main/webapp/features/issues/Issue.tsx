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
import { Card, Icon, Label } from 'semantic-ui-react';

import { IssueView, IssueViewSeverityEnum } from '../../services';
import { severityColor } from './utils';

const SeverityIcon = ({ severity }: { severity?: IssueViewSeverityEnum }) => {
  switch (severity) {
    default:
    case IssueViewSeverityEnum.CRITICAL:
      return <Icon name="warning sign" color={severityColor(severity)} />;
    case IssueViewSeverityEnum.MAJOR:
      return <Icon name="warning circle" color={severityColor(severity)} />;
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

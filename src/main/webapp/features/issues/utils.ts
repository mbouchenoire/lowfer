import { IssueViewSeverityEnum } from '../../services';

export const severityColor = (severity: IssueViewSeverityEnum | undefined) => {
  switch (severity) {
    case IssueViewSeverityEnum.BLOCKER:
      return 'red';
    case IssueViewSeverityEnum.CRITICAL:
      return 'orange';
    case IssueViewSeverityEnum.MAJOR:
      return 'yellow';
    case IssueViewSeverityEnum.MINOR:
      return 'olive';
  }
};

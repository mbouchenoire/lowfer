import { createSelector, createSlice, PayloadAction } from '@reduxjs/toolkit';

import { IssueView, IssueViewSeverityEnum } from '../../services';
import { RootState } from '../../store';
import { filter } from 'ramda';

export enum IssuesStatus {
  IDLE = 'IDLE',
  FETCHING = 'FETCHING',
  ERROR = 'ERROR'
}

export type IssueType = IssueView & {
  id: number | null;
};

interface IssuesState {
  activeIssue: {
    id: number | null;
    data: string;
    error?: string | null;
    status: IssuesStatus;
  };
  data: IssueType[];
  error?: string | null;
  status: IssuesStatus;
}

const initialState: IssuesState = {
  activeIssue: {
    id: null,
    data: '',
    error: null,
    status: IssuesStatus.FETCHING
  },
  data: [],
  error: null,
  status: IssuesStatus.FETCHING
};

export const slice = createSlice({
  name: 'issues',
  initialState,
  reducers: {
    get: (state) => {
      state.data = [];
      state.error = null;
      state.status = IssuesStatus.FETCHING;
    },
    getSuccess: (state, action: PayloadAction<IssueType[]>) => {
      state.data = action.payload;
      state.error = null;
      state.status = IssuesStatus.IDLE;
    },
    getFailure: (state, action: PayloadAction<string | undefined>) => {
      state.data = [];
      state.error = action.payload;
      state.status = IssuesStatus.ERROR;
    },
    setActiveIssueId: (state, action: PayloadAction<number | null>) => {
      state.activeIssue.id = action.payload;
    },
    getActiveIssue: (state) => {
      state.activeIssue.data = '';
      state.activeIssue.error = null;
      state.activeIssue.status = IssuesStatus.FETCHING;
    },
    getActiveIssueSuccess: (state, action: PayloadAction<string>) => {
      state.activeIssue.data = action.payload;
      state.activeIssue.error = null;
      state.activeIssue.status = IssuesStatus.IDLE;
    },
    getActiveIssueFailure: (
      state,
      action: PayloadAction<string | undefined>
    ) => {
      state.activeIssue.data = '';
      state.activeIssue.error = action.payload;
      state.activeIssue.status = IssuesStatus.ERROR;
    }
  }
});

export const { actions, reducer } = slice;

const getStatus = (state: RootState) => state.issues.status;

const get = (state: RootState) => state.issues.data;

const getActiveIssue = (state: RootState) => state.issues.activeIssue;

const createGetBySeverity = (severity: IssueViewSeverityEnum) =>
  createSelector<RootState, IssueType[], IssueType[]>(
    [get],
    filter((issue) => issue.severity === severity)
  );

const getBlockers = createGetBySeverity(IssueViewSeverityEnum.BLOCKER);
const getCriticals = createGetBySeverity(IssueViewSeverityEnum.CRITICAL);
const getMajors = createGetBySeverity(IssueViewSeverityEnum.MAJOR);
const getMinors = createGetBySeverity(IssueViewSeverityEnum.MINOR);

const getActiveIssueId = (state: RootState) => state.issues.activeIssue.id;

const getActiveIssueDot = (state: RootState) => state.issues.activeIssue.data;

const getActiveIssueStatus = (state: RootState) =>
  state.issues.activeIssue.status;

const getActiveIssueError = (state: RootState) =>
  state.issues.activeIssue.error;

const getActiveIssueData = (state: RootState) =>
  state.issues.data.find((issue) => issue.id === state.issues.activeIssue.id);

const getByKey = createSelector(
  [getBlockers, getCriticals, getMajors, getMinors],
  (blockers, criticals, majors, minors) => ({
    blockers,
    criticals,
    majors,
    minors
  })
);

const getCountByKey = createSelector([getByKey], (issues) =>
  Object.entries(issues).map(([type, val]: [string, IssueType[]]) => ({
    type,
    count: val.length
  }))
);

const getError = (state: RootState) => state.issues.error;

export const selectors = {
  get,
  getActiveIssue,
  getActiveIssueData,
  getActiveIssueDot,
  getActiveIssueError,
  getActiveIssueId,
  getActiveIssueStatus,
  getBlockers,
  getByKey,
  getCountByKey,
  getCriticals,
  getError,
  getMajors,
  getMinors,
  getStatus
};

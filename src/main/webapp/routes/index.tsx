import React, { ReactNode } from 'react';

import Issues from '../features/issues/Issues';
import Overview from '../features/overview/Overview';
import Studio from '../features/studio/Studio';

export enum RoutePath {
  ISSUES = '/issues',
  OVERVIEW = '/overview',
  DRAFT = '/draft'
}

interface Route {
  Component: ReactNode;
  label: string;
  path: RoutePath;
}

export const studio: Route = {
  Component: <Studio />,
  label: 'Studio',
  path: RoutePath.DRAFT
};

export const overview: Route = {
  Component: <Overview />,
  label: 'Overview',
  path: RoutePath.OVERVIEW
};

export const issues: Route = {
  Component: <Issues />,
  label: 'Issues',
  path: RoutePath.ISSUES
};

const routes: Route[] = [studio, overview, issues];

export default routes;

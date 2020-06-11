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

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

export const Basic = `name: Basic

components:
  - name: Frontend
    label: Customer Portal
    description: The customer portal frontend
    type: frontend
    dependencies:
      - component: API
      - component: Library

  - name: Library
    type: library

  - name: API
    type: service
    context: context1
    dependencies:
      - component: Database
      - component: Queue
        type: queue.publish

  - name: Database
    type: database
    context: context1

  - name: Queue
    type: queue
    context: context1

  - name: Script
    type: script
    context: context2
    dependencies:
      - component: Queue
        type: queue.subscribe
      - component: s3

  - name: s3
    label: Amazon S3
    type: object-storage
    context: context2
`;

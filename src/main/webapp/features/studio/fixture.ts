export const Basic = `name: Basic

components:
  - name: Frontend
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

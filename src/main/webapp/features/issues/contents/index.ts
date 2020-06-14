import { lazy } from 'react';

import { importMDX } from 'mdx.macro';

export default {
  APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE: lazy(() =>
    importMDX('./APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE.mdx')
  ),
  APPLY_RULE_OF_THREE: lazy(() => importMDX('./APPLY_RULE_OF_THREE.mdx')),
  APPLY_STABLE_DEPENDENCY_PRINCIPLE: lazy(() =>
    importMDX('./APPLY_STABLE_DEPENDENCY_PRINCIPLE.mdx')
  ),
  AVOID_DATABASE_MULTIPLE_CLIENTS: lazy(() =>
    importMDX('./AVOID_DATABASE_MULTIPLE_CLIENTS.mdx')
  ),
  AVOID_HAVING_SINGLE_DEVELOPER: lazy(() =>
    importMDX('./AVOID_HAVING_SINGLE_DEVELOPER.mdx')
  ),
  AVOID_QUEUE_MULTIPLE_PUBLISHERS: lazy(() =>
    importMDX('./AVOID_QUEUE_MULTIPLE_PUBLISHERS.mdx')
  ),
  AVOID_TRANSITIVE_DEPENDENCIES: lazy(() =>
    importMDX('./AVOID_TRANSITIVE_DEPENDENCIES.mdx')
  ),
  AVOID_UNSTABLE_LIBRARY: lazy(() => importMDX('./AVOID_UNSTABLE_LIBRARY.mdx'))
};

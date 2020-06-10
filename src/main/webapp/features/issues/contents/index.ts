import { lazy } from 'react';

import { importMDX } from 'mdx.macro';

export default {
  APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE: lazy(() =>
    importMDX('./APPLY_ACYCLIC_DEPENDENCIES_PRINCIPLE.mdx')
  ),
  APPLY_STABLE_DEPENDENCY_PRINCIPLE: lazy(() =>
    importMDX('./APPLY_STABLE_DEPENDENCY_PRINCIPLE.mdx')
  ),
  AVOID_DATABASE_MULTIPLE_CLIENTS: lazy(() =>
    importMDX('./AVOID_DATABASE_MULTIPLE_CLIENTS.mdx')
  ),
  APPLY_RULE_OF_THREE: lazy(() => importMDX('./APPLY_RULE_OF_THREE.mdx'))
};

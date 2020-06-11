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

import {
  actions,
  ArchitecturesStatus,
  selectors
} from '../../main/webapp/features/architectures/slice';
import { store } from '../../main/webapp/store';

const architectures = [
  {
    name: 'foo'
  },
  {
    name: 'bar'
  },
  {
    name: 'baz'
  }
];

describe('features/architectures', () => {
  describe('slice', () => {
    test('selectors', () => {
      // When
      store.dispatch(actions.getList());

      // Then
      expect(selectors.getStatus(store.getState())).toBe(
        ArchitecturesStatus.FETCHING
      );
      expect(selectors.getCurrentIndex(store.getState())).toBe(null);
      expect(selectors.getList(store.getState())).toEqual([]);

      // When
      store.dispatch(actions.getListSuccess(architectures));

      // Then
      expect(selectors.getList(store.getState())).toEqual(architectures);
      expect(selectors.getStatus(store.getState())).toBe(
        ArchitecturesStatus.IDLE
      );

      // When
      store.dispatch(actions.setIndex(1));

      // Then
      expect(selectors.getCurrentIndex(store.getState())).toBe(1);
      expect(selectors.getCurrentName(store.getState())).toBe('bar');

      // When
      store.dispatch(actions.getListFailure());
      expect(selectors.getStatus(store.getState())).toBe(
        ArchitecturesStatus.ERROR
      );
    });
  });
});

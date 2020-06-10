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

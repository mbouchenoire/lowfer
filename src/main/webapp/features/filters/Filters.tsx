import React, { useEffect } from 'react';
import { Dropdown, Loader, DropdownProps, Menu } from 'semantic-ui-react';
import { useDispatch, useSelector } from 'react-redux';

import { actions, selectors } from './slice';
import { MaintainerListItemView } from '../../services';
import Error from '../../components/Error';

import './styles.scss';

const Filters = () => {
  const components = useSelector(selectors.getComponents);
  const componentTypes = useSelector(selectors.getComponentTypes);
  const dispatch = useDispatch();
  const filters = useSelector(selectors.getFilters);
  const error = useSelector(selectors.getError);
  const isFetching = useSelector(selectors.getIsFetching);
  const maintainers = useSelector(selectors.getMaintainers);

  useEffect(() => {
    dispatch(actions.getInitial());
  }, [dispatch]);

  const createSetFilters = (name: any) => (
    e: any,
    { value }: DropdownProps
  ) => {
    dispatch(actions.setFilters({ name, value: value as string[] }));
  };

  return (
    <div className="Filters">
      <Menu vertical>
        <div className="ui small header">Filters</div>
        {error !== null && <Error error={error} />}

        {isFetching ? (
          <Loader active />
        ) : (
          <>
            {maintainers.length > 0 && (
              <Menu.Item className="Filter-item">
                <Dropdown
                  placeholder="Maintainers"
                  fluid
                  multiple
                  search
                  selection
                  onChange={createSetFilters('maintainers')}
                  value={filters.maintainers}
                  options={maintainers.map(
                    (maintainer: MaintainerListItemView) => ({
                      key: maintainer.name,
                      text: maintainer.name,
                      value: maintainer.name
                    })
                  )}
                />
              </Menu.Item>
            )}

            {componentTypes.length > 0 && (
              <Menu.Item className="Filter-item">
                <Dropdown
                  placeholder="Component types"
                  fluid
                  multiple
                  search
                  selection
                  onChange={createSetFilters('componentTypes')}
                  value={filters.componentTypes}
                  options={componentTypes.map((componentType) => ({
                    key: componentType.name,
                    text: componentType.label,
                    value: componentType.name
                  }))}
                />
              </Menu.Item>
            )}
            {components.length > 0 && (
              <Menu.Item className="Filter-item">
                <Dropdown
                  placeholder="Components"
                  fluid
                  multiple
                  search
                  selection
                  onChange={createSetFilters('components')}
                  value={filters.components}
                  options={components.map((component) => ({
                    key: component.name,
                    text: component.name,
                    value: component.name
                  }))}
                />
              </Menu.Item>
            )}
          </>
        )}
      </Menu>
    </div>
  );
};

export default Filters;

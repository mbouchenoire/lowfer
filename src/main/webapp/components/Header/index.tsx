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

import React, {
  SyntheticEvent,
  useCallback,
  useEffect,
  useMemo,
  useState
} from 'react';
import { Dropdown, Menu, Icon, Label } from 'semantic-ui-react';
import { SemanticCOLORS } from 'semantic-ui-react/dist/commonjs/generic';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory, Link, useLocation } from 'react-router-dom';

import {
  actions as studioActions,
  selectors as studioSelectors
} from '../../features/studio/slice';
import {
  actions as architecturesActions,
  selectors as architecturesSelectors
} from '../../features/architectures/slice';
import { selectors as filtersSelectors } from '../../features/filters/slice';
import {
  actions as issuesActions,
  selectors as issuesSelectors
} from '../../features/issues/slice';
import {
  actions as appActions,
  selectors as appSelectors
} from '../../features/app/slice';

import { ArchitectureSource } from '../../features/app/types';
import { RoutePath, studio, overview, issues } from '../../routes';
import sharingActions from '../../features/sharing/actions';

import './style.scss';

enum Prefix {
  ARCHITECTURE = 'architecture_',
  DRAFT = 'draft_'
}

const issueColor: Record<string, SemanticCOLORS> = {
  criticals: 'red',
  majors: 'orange',
  minors: 'yellow'
};

const Header = () => {
  const [justShared, setJustShared] = useState(false);
  const dispatch = useDispatch();
  const drafts = useSelector(studioSelectors.getDrafts);
  const currentStudioIndex = useSelector(studioSelectors.getCurrentIndex);
  const currentArchitectureIndex = useSelector(
    architecturesSelectors.getCurrentIndex
  );
  const architectures = useSelector(architecturesSelectors.getList);
  const currentArchitectureName = useSelector(
    architecturesSelectors.getCurrentName
  );
  const currentStudioName = useSelector(studioSelectors.getCurrentName);
  const componentCount = useSelector(filtersSelectors.getComponentCount);
  const issuesCount = useSelector(issuesSelectors.getCountByKey);
  const history = useHistory();
  const { pathname } = useLocation();
  const architectureSource = useSelector(appSelectors.getArchitectureSource);

  useEffect(() => {
    dispatch(issuesActions.get());
  }, [dispatch]);

  const currentItem = useMemo(() => {
    if (!architectureSource) return { name: undefined, value: undefined };
    if (architectureSource === ArchitectureSource.LOCAL)
      return {
        name: currentStudioName,
        value: `${Prefix.DRAFT}${currentStudioIndex}`
      };
    return {
      name: currentArchitectureName,
      value: `${Prefix.ARCHITECTURE}${currentArchitectureIndex}`
    };
  }, [
    architectureSource,
    currentArchitectureIndex,
    currentArchitectureName,
    currentStudioIndex,
    currentStudioName
  ]);

  const isArchitectureLocalStorage = (val: string | undefined) =>
    val?.startsWith(Prefix.DRAFT);

  const isArchitectureVersioned = (val: string | undefined) =>
    val?.startsWith(Prefix.ARCHITECTURE);

  const onSelection = useCallback(
    (e: SyntheticEvent<HTMLElement, Event>, data: any) => {
      const val = String(data.value);
      if (isArchitectureVersioned(val)) {
        dispatch(
          architecturesActions.setIndex(
            Number(val.split(Prefix.ARCHITECTURE)[1])
          )
        );
        history.push(RoutePath.OVERVIEW);
      }
      if (isArchitectureLocalStorage(val)) {
        dispatch(studioActions.setIndex(Number(val.split(Prefix.DRAFT)[1])));
        history.push(RoutePath.DRAFT);
      }
    },
    [dispatch, history]
  );

  const addDraft = useCallback(() => {
    dispatch(studioActions.addDraft());
    dispatch(appActions.setSource(ArchitectureSource.LOCAL));
    history.push(RoutePath.DRAFT);
  }, [dispatch, history]);

  const createRemoveDraft = useCallback(
    (index: number) => () => {
      dispatch(studioActions.removeDraft(index));
    },
    [dispatch]
  );

  const share = useCallback(() => {
    dispatch(sharingActions.share);
    setJustShared(true);
    setTimeout(() => {
      setJustShared(false);
    }, 5000);
  }, [dispatch]);

  const isStudioDisabled = architectureSource === ArchitectureSource.VERSIONNED;

  const totalIssueCount = Object.values(issuesCount).reduce(
    (sum, current) => sum + current.count,
    0
  );

  const staticDropdownItems = architectures.map(
    ({ name = '' }, index: number) => (
      <Dropdown.Item
        key={`${Prefix.ARCHITECTURE}${index}`}
        value={`${Prefix.ARCHITECTURE}${index}`}
        text={name}
        onClick={onSelection}
      />
    )
  );

  const draftDropdownItems = drafts.map(({ key = '' }, index: number) => (
    <Dropdown.Item
      className="Architecture-draftItem"
      key={`${Prefix.DRAFT}${index}`}
      onClick={onSelection}
      value={`${Prefix.DRAFT}${index}`}
    >
      {key}
      <Icon
        className="Architecture-deleteDraft"
        color="red"
        name="remove"
        onClick={createRemoveDraft(index)}
        size="mini"
      />
    </Dropdown.Item>
  ));

  const trigger = (
    <span className="Architecture-select">
      <Icon
        name={isArchitectureLocalStorage(currentItem.value) ? 'lab' : 'fork'}
      />
      {currentItem.name || 'Select an architecture'}
    </span>
  );

  return (
    <Menu size="massive" color="blue">
      <Menu.Item name="Lowfer" />
      <Menu.Item className="Header-name">
        <Dropdown
          onChange={onSelection}
          value={currentItem.value || ''}
          trigger={trigger}
        >
          <Dropdown.Menu>
            <Dropdown.Header icon="lab" content=" local storage" />
            {draftDropdownItems}
            <Dropdown.Item
              icon="plus"
              key="add-draft"
              onClick={addDraft}
              text="Add draft"
            />
            <Dropdown.Divider />
            <Dropdown.Header icon="fork" content=" Versioned" />
            {staticDropdownItems}
          </Dropdown.Menu>
        </Dropdown>
      </Menu.Item>
      <Menu.Menu>
        <Link className="Header-link" to={isStudioDisabled ? '#' : studio.path}>
          <Menu.Item
            active={studio.path === pathname}
            disabled={isStudioDisabled}
          >
            {studio.label}
          </Menu.Item>
        </Link>
        <Link className="Header-link" to={overview.path}>
          <Menu.Item active={overview.path === pathname}>
            {overview.label}
            <Label>{componentCount} components</Label>
          </Menu.Item>
        </Link>
        <Link className="Header-link" to={issues.path}>
          <Menu.Item active={issues.path === pathname}>
            {issues.label}
            {issuesCount.map(({ type, count }) =>
              count === 0 ? null : (
                <Label
                  circular
                  className="Issue-labels"
                  color={issueColor[type]}
                  key={type}
                >
                  {count}
                </Label>
              )
            )}
            {totalIssueCount === 0 && <Label>none</Label>}
          </Menu.Item>
        </Link>
      </Menu.Menu>
      <Menu.Menu position="right">
        {(currentArchitectureName || currentStudioIndex !== null) && (
          <Menu.Item className="Share" key="share" onClick={share}>
            {justShared ? (
              <span className="Share-justShared">
                <Icon name="share square" />
                Link successfully copied to clipboard
              </span>
            ) : (
              <>
                <Icon name="linkify" />
                Link this view
              </>
            )}
          </Menu.Item>
        )}
      </Menu.Menu>
    </Menu>
  );
};

export default Header;

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

import { StudioDraft } from './slice';
import { Basic } from './fixture';

const localStorageKey = '@@lowfer/drafts';

export const saveStateToLocalStorage = (drafts: StudioDraft[]) =>
  localStorage.setItem(
    localStorageKey,
    JSON.stringify(drafts.map((draft) => ({ key: draft.key, raw: draft.raw })))
  );

export const getStateToLocalStorage = () => {
  const saved = localStorage.getItem(localStorageKey);
  return saved ? JSON.parse(saved) : [{ key: 'Basic', raw: Basic }];
};

export const copyToClipboard = (str: string) => {
  const el = document.createElement('textarea');
  el.value = str;
  document.body.appendChild(el);
  el.select();
  document.execCommand('copy');
  document.body.removeChild(el);
};

const studioKeyRegex = /name: (.+)/m;

export const getStudioKeyFromRaw = (raw: string): string | undefined => {
  const nameGroups = studioKeyRegex.exec(raw);
  if (nameGroups === null) return;
  return nameGroups[1];
  return;
};

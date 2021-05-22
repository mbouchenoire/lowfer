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

const mutationCallback = (mutationsList: MutationRecord[]) => {
  for (const mutation of mutationsList) {
    if (mutation.type !== 'attributes') return;

    const target = mutation.target as HTMLLIElement;
    const targetId = target.id || 'svg';
    if (!targetId) return;

    try {
      document
        .querySelector(`.Graph-coarsed #${targetId}`)
        ?.setAttribute('transform', target.getAttribute('transform') || '');
    } catch (err) {
      console.log(err);
    }
  }
};

export const mutationObserver = new MutationObserver(mutationCallback);

export const mutationConfig: MutationObserverInit = {
  attributes: true
};

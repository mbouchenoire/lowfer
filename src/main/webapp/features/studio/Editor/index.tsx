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

import React, { useState, useEffect } from 'react';
import AceEditor from 'react-ace';
import { useDebounce } from 'react-use';
import { useSelector, useDispatch } from 'react-redux';
import 'ace-builds/src-noconflict/theme-chrome';

import { actions, selectors } from '../slice';
import './options';

const Editor = () => {
  const dispatch = useDispatch();
  const raw = useSelector(selectors.getCurrentRaw);

  const [value, setValue] = useState(raw);

  useEffect(() => {
    setValue(raw);
  }, [setValue, raw]);

  useDebounce(
    () => {
      dispatch(actions.setRaw(value));
    },
    500,
    [dispatch, value]
  );

  return (
    <AceEditor
      className="Editor"
      editorProps={{ $blockScrolling: true }}
      enableBasicAutocompletion
      enableLiveAutocompletion
      enableSnippets
      height="100%"
      mode="yaml"
      name="studio"
      onChange={setValue}
      theme="chrome"
      value={value}
    />
  );
};

export default Editor;

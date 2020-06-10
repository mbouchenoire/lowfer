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

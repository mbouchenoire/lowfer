import React, { useEffect } from 'react';
import { isNil } from 'ramda';
import { useSelector, useDispatch } from 'react-redux';
import { Message } from 'semantic-ui-react';

import { actions as filtersActions } from '../../filters/slice';
import { selectors } from '../slice';
import Editor from '../Editor';
import Error from '../../../components/Error';
import Graph from '../Graph';

import './styles.scss';

const Studio = () => {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(filtersActions.resetFilters());
  }, []);

  const currentStudioIndex = useSelector(selectors.getCurrentIndex);

  const error = useSelector(selectors.getError);

  console.log({ currentStudioIndex });

  if (isNil(currentStudioIndex)) {
    return (
      <Message
        className="NoDraft"
        icon="info"
        info
        header="No draft selected"
        content="Please select a draft"
      />
    );
  }

  return (
    <>
      <Editor />
      <div className="Studio-graph">
        {error !== null ? <Error error={error} /> : <Graph />}
      </div>
    </>
  );
};

export default Studio;

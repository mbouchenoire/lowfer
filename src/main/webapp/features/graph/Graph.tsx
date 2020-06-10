import React, { useEffect } from 'react';
import { Loader } from 'semantic-ui-react';
import { useSelector, useDispatch } from 'react-redux';
import classnames from 'classnames';

import { actions, selectors } from './slice';

import './styles.scss';
import Settings from './Settings';

type Props = {
  dot?: string;
  isFetching: boolean;
};

const Graph = ({ dot, isFetching }: Props) => {
  const coarsed = useSelector(selectors.getCoarsed);
  const coarsedContent = useSelector(selectors.getCoarsedContent);
  const dispatch = useDispatch();
  const graphRendered = useSelector(selectors.getGraphRendered);

  useEffect(() => {
    if (!dot || dot === '') return;
    dispatch(actions.renderGraph(dot));
  }, [dot]);

  return (
    <div className={classnames('Graph', { coarsed })}>
      <Settings dot={dot} />
      <div
        className={classnames('Graph-clean', {
          hidden: isFetching || !graphRendered,
          coarsed
        })}
        id="#graph"
      />
      <Loader active={!graphRendered || isFetching} size="huge" />
      {coarsedContent !== '' && (
        <div
          className="Graph-coarsed"
          dangerouslySetInnerHTML={{ __html: coarsedContent }}
        />
      )}
    </div>
  );
};

export default Graph;

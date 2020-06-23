import React, { useCallback } from 'react';
import { Icon } from 'semantic-ui-react';
import { saveSvgAsPng } from 'save-svg-as-png';
import { useSelector } from 'react-redux';
import { selectors } from './slice';

const Save = () => {
  const coarsed = useSelector(selectors.getCoarsed);

  const saveGraph = useCallback(() => {
    const graphElm = document.querySelector(
      `${coarsed ? '.Graph-coarsed' : '#graph'} > svg`
    );
    if (!graphElm) return;
    saveSvgAsPng(graphElm, `lowfer-graph_${Date.now()}`, {
      backgroundColor: 'white',
      encoderOptions: 1,
      scale: 3
    });
  }, [coarsed]);

  return (
    <div className="Graph-save">
      <Icon name="save outline" size="big" onClick={saveGraph} />
    </div>
  );
};

export default Save;

import React from 'react';
import { Message } from 'semantic-ui-react';

import './styles.scss';

type Props = {
  error?: string | null;
};

const Error = ({
  error = 'Something went wrong. Please try again in a moment'
}: Props) => (
  <div className="Error">
    <Message icon="warning" error header="Error" content={error} />
  </div>
);

export default Error;

import { createAction } from '@reduxjs/toolkit';

import { ShareActionTypes } from './types';

const share = createAction(ShareActionTypes.SHARE)();

export default {
  share
};

import { ArchitectureResourceApiFp } from './api';

export const architectureResourceApi = ArchitectureResourceApiFp();

export const basePath =
  process.env.REACT_APP_API_HOST || window.location.origin;

export enum ArchitectureSource {
  LOCAL = 'LOCAL',
  VERSIONNED = 'VERSIONNED'
}

export interface AppState {
  architectureSource: ArchitectureSource | null;
}

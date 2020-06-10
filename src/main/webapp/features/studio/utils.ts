import { StudioDraft } from './slice';
import { Basic } from './fixture';

const localStorageKey = '@@lowfer/drafts';

export const saveStateToLocalStorage = (drafts: StudioDraft[]) =>
  localStorage.setItem(
    localStorageKey,
    JSON.stringify(drafts.map((draft) => ({ key: draft.key, raw: draft.raw })))
  );

export const getStateToLocalStorage = () => {
  const saved = localStorage.getItem(localStorageKey);
  return saved ? JSON.parse(saved) : [{ key: 'Basic', raw: Basic }];
};

export const copyToClipboard = (str: string) => {
  const el = document.createElement('textarea');
  el.value = str;
  document.body.appendChild(el);
  el.select();
  document.execCommand('copy');
  document.body.removeChild(el);
};

const studioKeyRegex = /name: (.+)/m;

export const getStudioKeyFromRaw = (raw: string): string | undefined => {
  const nameGroups = studioKeyRegex.exec(raw);
  if (nameGroups === null) return;
  return nameGroups[1];
  return;
};

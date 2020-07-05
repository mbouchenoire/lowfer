import * as monaco from 'monaco-editor';

import { architectureResourceApi } from '../../../services/config';
import { selectors as filtersSelectors } from '../../filters/slice';
import { store } from '../../../store';

monaco.editor.defineTheme('lowfer', {
  base: 'vs', // can also be vs-dark or hc-black
  colors: {
    'editor.background': '#f9f9f9'
  },
  inherit: true, // can also be false to completely replace the builtin rules
  rules: []
});

const createComponentTypesProposals = async (range: any) => {
  const componentsData = await architectureResourceApi.getComponentTypesUsingGET()();
  const componentsList = componentsData.data.componentTypes;
  return componentsList?.map((component) => ({
    label: component.name,
    kind: monaco.languages.CompletionItemKind.Function,
    documentation: component.label,
    insertText: component.name,
    range
  }));
};

const createComponentNamesProposals = (range: any) => {
  const components = filtersSelectors.getComponents(store.getState());
  return components?.map((component) => ({
    label: component.name,
    kind: monaco.languages.CompletionItemKind.Function,
    documentation: component.name,
    insertText: component.name,
    range
  }));
};

monaco.languages.registerCompletionItemProvider('yaml', {
  triggerCharacters: [':', ' '],
  // @ts-ignore
  provideCompletionItems: (model, position) => {
    const textUntilPosition = model.getValueInRange({
      startLineNumber: position.lineNumber,
      startColumn: 1,
      endLineNumber: position.lineNumber,
      endColumn: position.column
    });
    const word = model.getWordUntilPosition(position);
    const range = {
      startLineNumber: position.lineNumber,
      endLineNumber: position.lineNumber,
      startColumn: word.startColumn,
      endColumn: word.endColumn
    };
    const namesMatch = textUntilPosition.match(/component:/);
    if (namesMatch) {
      return {
        suggestions: createComponentNamesProposals(range)
      };
    }
    const typesMatch = textUntilPosition.match(/type:/);
    if (!typesMatch) {
      return { suggestions: [] };
    }

    return new Promise((resolve) => {
      createComponentTypesProposals(range).then((suggestions) => {
        resolve({
          // @ts-ignore
          suggestions
        });
      });
    });
  }
});

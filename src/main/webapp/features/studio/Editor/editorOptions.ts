import * as monaco from 'monaco-editor';

console.log({ monaco });

monaco.editor.defineTheme('lowfer', {
  base: 'vs', // can also be vs-dark or hc-black
  colors: {
    'editor.background': '#f9f9f9'
  },
  inherit: true, // can also be false to completely replace the builtin rules
  rules: []
});

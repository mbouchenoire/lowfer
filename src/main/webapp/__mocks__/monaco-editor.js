const editor = {
  create: () => ({
    dispose: () => undefined
  }),
  defineTheme: () => undefined
};

const languages = {
  registerCompletionItemProvider: () => undefined
};

const monaco = {
  editor,
  languages
};

module.exports = monaco;

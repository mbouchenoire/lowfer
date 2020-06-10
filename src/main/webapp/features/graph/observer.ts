const mutationCallback = (mutationsList: MutationRecord[]) => {
  for (const mutation of mutationsList) {
    if (mutation.type !== 'attributes') return;
    const target = mutation.target as HTMLLIElement;
    const targetId = target.id || 'svg';
    if (!targetId) return;
    try {
      document
        .querySelector(`.Graph-coarsed #${targetId}`)
        ?.setAttribute('transform', target.getAttribute('transform') || '');
    } catch (err) {
      console.log(err);
    }
  }
};

export const mutationObserver = new MutationObserver(mutationCallback);

export const mutationConfig = {
  attributes: true,
  childList: true,
  subtree: true
};

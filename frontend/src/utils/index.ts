import { app } from "@use/useAegir";
import { createApp } from "vue";

export function getEmitsObject(emitsArray) {
  const emitsObject = {};

  emitsArray.forEach(val => {
    emitsObject[val] = () => true;
  });

  return emitsObject;
}

export function createUuid(): string {
  let dt = new Date().getTime();
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(
    /[xy]/g,
    function(c) {
      const r = (dt + Math.random() * 16) % 16 | 0;
      dt = Math.floor(dt / 16);
      return (c == "x" ? r : (r & 0x3) | 0x8).toString(16);
    },
  );
}

export function createChildApp(appCfg) {
  const childApp = createApp(appCfg);

  childApp.config.globalProperties = app.config.globalProperties;

  const { ...appContext } = app._context;
  Object.assign(childApp._context, appContext);

  return app;
}

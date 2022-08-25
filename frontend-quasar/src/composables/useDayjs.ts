import dayjs, { PluginFunc } from "dayjs";

export default function useDayjs() {
  const addPlugin = (plugin: PluginFunc) => {
    dayjs.extend(plugin);
  };

  return {
    dayjs,
    addPlugin,
  };
}

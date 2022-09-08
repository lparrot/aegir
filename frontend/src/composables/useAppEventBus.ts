import emitter from "tiny-emitter/instance";

export const Bus = {
  $on: (...args) => emitter.on(...args),
  $once: (...args) => emitter.once(...args),
  $off: (...args) => emitter.off(...args),
  $emit: (...args) => emitter.emit(...args),
};

export default function useAppEventBus() {
  return Bus;
};

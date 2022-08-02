import { ref } from "vue";
import { Client, over, Subscription } from "webstomp-client";
import * as SockJS from "sockjs-client/dist/sockjs";
import useAppLocalStorage from "src/composables/useAppLocalStorage";

const client = ref<Client>();
const socket = ref<SockJS>();
const { storageToken } = useAppLocalStorage();

export default function useWebsocket() {

  const initialize = (url: string) => {
    socket.value = new SockJS(url);
    client.value = over(socket.value, { debug: false });
  };

  const connect = () => {
    const headers = {};
    headers["Authorization"] = storageToken.value;

    return new Promise((resolve, reject) => {
      client.value.connect(
        headers,
        frame => { resolve(client); },
        error => {
          resolve(error);
        },
      );
    });
  };

  const disconnect = () => {
    client.value.disconnect();
  };

  const onMessage = (topic: string, callback: (message: any) => void): Subscription => {
    return client.value.subscribe(topic, async (message) => {
      await callback(JSON.parse(message.body));
    });
  };

  return {
    client,
    socket,
    onMessage,
    initialize,
    connect,
    disconnect,
  };
}

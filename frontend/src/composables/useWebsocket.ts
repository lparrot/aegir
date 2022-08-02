import { ref } from "vue";
// import { Client, over, Subscription } from "webstomp-client";
import Stomp from "stompjs";
import SockJS from "sockjs-client/dist/sockjs";
import useAppLocalStorage from "src/composables/useAppLocalStorage";

const client = ref<Stomp.Client>();
const socket = ref<SockJS>();
const isConnected = ref<boolean>(false);
const { storageToken } = useAppLocalStorage();

export default function useWebsocket() {

  const initialize = (url: string) => {
    socket.value = new SockJS(url, null, { timeout: 2000 });
    client.value = Stomp.over(socket.value);
  };

  const connect = () => {
    const headers = {};
    if (storageToken.value != null) {
      headers["Authorization"] = storageToken.value;
    }

    return new Promise((resolve, reject) => {
      client.value.connect(
        headers,
        frame => {
          isConnected.value = true;
          resolve(client);
        },
        error => {
          reject(error);
        },
      );
    });
  };

  const disconnect = () => {
    client.value.disconnect(() => { console.log("disconnected");});
  };

  const subscribe = async (topic: string, callback: (message: any) => void): Promise<any> => {
    return client.value.subscribe(topic, async (message) => {
      await callback(JSON.parse(message.body));
    });
  };

  return {
    client,
    socket,
    subscribe,
    initialize,
    connect,
    disconnect,
  };
}

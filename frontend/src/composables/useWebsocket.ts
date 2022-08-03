import { ref } from "vue";
import { Client } from "@stomp/stompjs";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import SockJS from "sockjs-client/dist/sockjs";

const client = ref<Client>();
const isConnected = ref<boolean>(false);
const { storageToken } = useAppLocalStorage();

export default function useWebsocket() {
  const initialize = () => {
    if (client.value == null || !client.value.connected) {
      const headers = {};
      if (storageToken.value != null) {
        headers["Authorization"] = storageToken.value;
      }

      client.value = new Client({
        connectHeaders: headers,
        debug: function(message) {
          console.log(message);
        },
        webSocketFactory: () => {
          return new SockJS("/ws");
        },
      });
    }
  };

  const connect = () => {
    return new Promise(resolve => {
      client.value.activate();

      client.value.onConnect = receipt => {
        isConnected.value = true;
        resolve(receipt);
      };
    });
  };

  const disconnect = async () => {
    isConnected.value = false;
    await client.value.deactivate();
  };

  const subscribe = async (topic: string, callback: (message: any) => void): Promise<any> => {
    return client.value.subscribe(topic, async (message) => {
      await callback(JSON.parse(message.body));
    });
  };

  return {
    client,
    initialize,
    connect,
    subscribe,
    disconnect,
  };
}

import { ref } from "vue";
import { Client } from "@stomp/stompjs";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import { useBrowserLocation } from "@vueuse/core";

const client = ref<Client>();
const isConnected = ref<boolean>(false);
const { storageToken } = useAppLocalStorage();

export default function useWebsocket() {
  const connect = () => {
    return new Promise((resolve, reject) => {
      if (client.value == null) {
        const location = useBrowserLocation(window);
        const url = `${ location.value.protocol === "https:" ? "wss" : "ws" }://${ location.value.host }/ws`;

        const headers = {};
        if (storageToken.value != null) {
          headers["Authorization"] = storageToken.value;
        }

        client.value = new Client({
          // brokerURL: url,
          connectHeaders: headers,
          debug: function(message) {
            console.log(message);
          },
          webSocketFactory: () => {
            return new WebSocket(url);
          },
        });

        client.value.activate();

        client.value.onConnect = receipt => {
          isConnected.value = true;
          resolve(receipt);
        };
      }
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
    connect,
    subscribe,
    disconnect,
  };
}

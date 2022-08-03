import { ref } from "vue";
import { Client, StompSubscription } from "@stomp/stompjs";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import SockJS from "sockjs-client/dist/sockjs";
import { Dialog, DialogChainObject } from "quasar";
import ApplicationCloseMessage from "components/ApplicationCloseMessage.vue";

const client = ref<Client>();
const dialog = ref<DialogChainObject>();

const isConnected = ref<boolean>(false);
const { storageToken } = useAppLocalStorage();

export default function useWebsocket() {
  const initialize = () => {
    const headers = {};
    if (storageToken.value != null) {
      headers["Authorization"] = storageToken.value;
    }

    client.value = new Client({
      connectHeaders: headers,
      debug: msg => {
        console.log(msg);
      },
      webSocketFactory: () => {
        return new SockJS("/ws");
      },
    });
  };

  const connect = () => {
    return new Promise(resolve => {
      client.value.activate();

      client.value.onConnect = receipt => {
        if (dialog.value != null) {
          dialog.value.hide();
          dialog.value = null;
        }
        isConnected.value = true;
        resolve(receipt);
      };

      client.value.onWebSocketClose = (event: CloseEvent) => {
        if (event.code === 1000) {
          /* Normal close */
          return;
        }

        if (dialog.value == null) {
          dialog.value = Dialog.create({
            component: ApplicationCloseMessage,
          });
        }
      };
    });
  };

  const disconnect = async () => {
    isConnected.value = false;
    await client.value.deactivate();
  };

  const subscribe = async (topic: string, callback: (message: any) => void): Promise<StompSubscription> => {
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

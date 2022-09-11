import { Client, StompHeaders, StompSubscription } from "@stomp/stompjs";
import useAegir from "@use/useAegir";
import useAppLocalStorage from "@use/useAppLocalStorage";
import { until } from "@vueuse/core";
import SockJS from "sockjs-client/dist/sockjs";
import DialogApplicationClosed from "src/components/dialogs/DialogApplicationClosed.vue";
import { ref } from "vue";

const client = ref<Client>(null);
const dialogInstance = ref();

const isConnected = ref<boolean>(false);
const { storageToken } = useAppLocalStorage();

export const getHeaders = () => {
  const headers = {};
  if (storageToken.value != null) {
    headers["Authorization"] = storageToken.value;
  }
  return headers;
};

export default function useWebsocket() {
  const { dialog } = useAegir();

  const initialize = () => {
    client.value = new Client({
      connectHeaders: getHeaders(),
      reconnectDelay: 10000,
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
        if (dialogInstance.value != null) {
          dialogInstance.value.hide();
          dialogInstance.value = null;
        }
        isConnected.value = true;
        resolve(receipt);
      };

      client.value.onWebSocketClose = (event: CloseEvent) => {
        if (event.code === 1001) {
          /* CLOSE_GOING_AWAY	Reçu lorsque une erreur est apparue sur le serveur ou le navigateur quitte la page ayant ouvert la connexion. */
          if (dialogInstance.value == null) {
            dialogInstance.value = dialog.create({
              component: DialogApplicationClosed,
              showButtons: false,
            });
          }
        }
      };
    });
  };

  const disconnect = async () => {
    isConnected.value = false;
    await client.value.deactivate();
  };

  const subscribe = async (topic: string, callback: (message: any) => void, headers?: StompHeaders): Promise<StompSubscription> => {
    if (client.value == null) {
      initialize();
    }

    if (client.value != null) {
      if (!client.value.active) {
        await connect();
      }

      await until(isConnected).toBe(true);

      return client.value.subscribe(topic, async (message) => {
        callback(JSON.parse(message.body));
      }, { ...headers });
    }
  };

  const unsubscribe = (id: string) => {
    if (client.value != null) {
      client.value.unsubscribe(id, { ...getHeaders() });
    }
  };

  return {
    client,
    initialize,
    connect,
    subscribe,
    unsubscribe,
    disconnect,
  };
}

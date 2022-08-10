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

export const getHeaders = () => {
  const headers = {};
  if (storageToken.value != null) {
    headers["Authorization"] = storageToken.value;
  }
  return headers;
};

export default function useWebsocket() {
  const initialize = () => {
    client.value = new Client({
      connectHeaders: getHeaders(),
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
        if (event.code === 1001) {
          /* CLOSE_GOING_AWAY	Reçu lorsque une erreur est apparue sur le serveur ou le navigateur quitte la page ayant ouvert la connexion. */
          if (dialog.value == null) {
            dialog.value = Dialog.create({
              component: ApplicationCloseMessage,
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

  const subscribe = async (topic: string, callback: (message: any) => void): Promise<StompSubscription> => {
    if (client.value == null) {
      await initialize();
    }

    if (!client.value.active) {
      await connect();
    }

    return client.value.subscribe(topic, async (message) => {
      await callback(JSON.parse(message.body));
    }, getHeaders());
  };

  return {
    client,
    initialize,
    connect,
    subscribe,
    disconnect,
  };
}

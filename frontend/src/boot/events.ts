import { boot } from "quasar/wrappers";
import { watch } from "vue";
import { useProjectStore } from "stores/project";
import useAppEventBus from "src/composables/useAppEventBus";
import useMenu from "src/composables/useMenu";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import useWebsocket from "src/composables/useWebsocket";
import { Notify } from "quasar";
import { useAuthStore } from "stores/auth";
import { EnumWebsocketMessageType, WebsocketTypedMessage } from "app/.generated/rest";

const authStore = useAuthStore();
const socket = useWebsocket();
const projectStore = useProjectStore();
const bus = useAppEventBus();
const { refreshMenu } = useMenu();
const { storageSidebar } = useAppLocalStorage();

export default boot(async ({ app, router }) => {
  bus.$on("connected", async () => {
    if (authStore.user != null) {
      await projectStore.fetchUserProjects();
      await projectStore.fetchSelectedProject();
      await projectStore.fetchSelectedItem();

      refreshMenu();

      await socket.subscribe("/topic/session", async (message: WebsocketTypedMessage) => {
        switch (message.type) {
          case EnumWebsocketMessageType.CONNECT:
          case EnumWebsocketMessageType.DISCONNECT:
            Notify.create({
              message: `${message.type}: ${message.data?.user}`,
              color: "info",
            });
            break;
          default:
            break;
        }
      });

      await socket.subscribe("/user/topic/session", async (message: WebsocketTypedMessage) => {
        switch (message.type) {
          case EnumWebsocketMessageType.CLOSE_SESSION:
            await authStore.disconnect();
            Notify.create({
              message: `Vous avez été déconnecté par l'action d'un administrateur`,
              color: "warning",
            });
            break;
          default:
            break;
        }
      });
    }
  });

  bus.$on("disconnected", () => {
    socket.disconnect();
  });

  bus.$on("update:projects", async () => {
    await projectStore.fetchUserProjects();
  });

  watch(() => storageSidebar?.value?.project_selected,
    async (projectSelected) => {
      if (projectSelected != null) {
        await projectStore.fetchSelectedProject();
      } else {
        projectStore.selectedProject = null;
      }
    },
    { deep: true },
  );

  watch(() => storageSidebar?.value?.item_selected,
    async (itemSelected) => {
      if (itemSelected != null) {
        await projectStore.fetchSelectedItem();
      } else {
        projectStore.selectedItem = null;
      }
    },
    { deep: true },
  );
});

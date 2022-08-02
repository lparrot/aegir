import { boot } from "quasar/wrappers";
import { watch } from "vue";
import { useProjectStore } from "stores/project";
import useAppEventBus from "src/composables/useAppEventBus";
import useMenu from "src/composables/useMenu";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import useWebsocket from "src/composables/useWebsocket";
import { Notify } from "quasar";

const socket = useWebsocket();
const projectStore = useProjectStore();
const bus = useAppEventBus();
const { refreshMenu } = useMenu();
const { storageSidebar } = useAppLocalStorage();

export default boot(async ({ app, router }) => {
  bus.$on("connected", async () => {
    await projectStore.fetchUserProjects();
    await projectStore.fetchSelectedProject();
    await projectStore.fetchSelectedItem();
    refreshMenu();

    await socket.connect();

    socket.onMessage("/topic/session", message => {
      Notify.create({
        message: `${message.type}: ${message.data?.user}`,
        color: "info",
      });
    });
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

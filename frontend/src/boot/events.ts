import { boot } from "quasar/wrappers";
import { watch } from "vue";
import { useProjectStore } from "stores/project";
import useAegir from "src/composables/useAegir";

const projectStore = useProjectStore();
const { bus, refreshMenu, storageSidebar } = useAegir();

export default boot(async ({ app, router }) => {
  bus.$on("connected", async () => {
    await projectStore.fetchUserProjects();
    await projectStore.fetchSelectedItem();
    refreshMenu();
  });

  bus.$on("update:projects", async () => {
    await projectStore.fetchUserProjects();
  });

  watch(() => storageSidebar?.value?.project_selected,
    async (projectSelected) => {
      if (projectSelected != null) {
        await projectStore.fetchSelectedProject();
      }
    },
    { deep: true, immediate: true },
  );

  // watch(() => storageSidebar?.value?.item_selected,
  //   async (itemSelected) => {
  //     if (itemSelected != null) {
  //       await projectStore.fetchSelectedItem();
  //     }
  //   },
  //   { deep: true, immediate: true },
  // );
});

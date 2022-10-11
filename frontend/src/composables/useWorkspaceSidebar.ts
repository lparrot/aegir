import { WorkspaceInfo_Children } from "back_types";
import { computed, Ref, watch } from "vue";

const { storageSidebar } = useAppLocalStorage();

interface UseWorkspaceSidebarComposableOptions {
  with_watch: boolean;
}

export default function useWorkspaceSidebar(options: UseWorkspaceSidebarComposableOptions = { with_watch: false }) {
  const workspaces = ref([]);
  const selectedWorkspace: Ref<WorkspaceInfo_Children> = ref();

  const fetchWorkspaces = async () => {
    const { result } = await api.getWorkspacesByCurrentUser();
    workspaces.value = result;
  };

  const fetchWorkspaceDetail = async () => {
    const { result } = await api.getWorkspaceById(storageSidebar.value.workspace_selected);
    selectedWorkspace.value = result;
  };

  const selectedWorkspaceName = computed(() => {
    return selectedWorkspace.value?.name;
  });

  if (options.with_watch) {
    watch(() => storageSidebar.value.workspace_selected,
      async (value: number) => {
        if (value != null) {
          await fetchWorkspaceDetail();
        }
      },
      { immediate: true },
    );
  }

  return {
    workspaces,
    fetchWorkspaces,
    fetchWorkspaceDetail,
    selectedWorkspace,
    selectedWorkspaceName,
  };
}

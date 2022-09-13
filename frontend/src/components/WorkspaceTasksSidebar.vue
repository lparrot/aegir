<template>
  <aside v-if="route.meta?.with_workspaces" ref="drawer_sidebar" :class="{'max-w-[25px]': isSidebarClosed, 'max-w-full': !isSidebarClosed, 'fixed': isMobile}" class="relative flex flex-col flex-shrink-0 bg-primary-200 w-72 text-white transition-all duration-150 ease-in h-screen rounded-r-2xl border border-primary-300 shadow-2xl">
    <div class="absolute top-4 -right-4 rounded-full bg-white h-7 w-7 flex items-center justify-center border border-primary-300 hover:bg-primary-100 cursor-pointer" @click="toggle">
      <mdi-chevron-right v-if="isSidebarClosed" class="h-5 w-5 text-primary"/>
      <mdi-chevron-left v-else class="h-5 w-5 text-primary"/>
    </div>

    <div v-show="!isSidebarClosed" class="p-5 text-primary">
      <div class="flex justify-between ">
        <div>Espace de travail</div>
        <div>
          <mdi-dots-horizontal class="h-5 w-5 cursor-pointer"/>
        </div>
      </div>

      <Select v-model="storageSidebar.workspace_selected" :placeholder="selectedWorkspaceName" class="mt-4" null-label="Choisissez...">
        <SelectItem v-for="workspace in workspaces" :key="workspace.id" :label="workspace.name" :value="workspace.id"/>
      </Select>

      <div class="grid grid-cols-1 text-primary-600 mt-4 gap-3">
        <div class="flex items-center gap-2 cursor-pointer -m-1 p-1 rounded hover:bg-primary-300">
          <mdi-plus class="h-5 w-5"/>
          Ajouter
        </div>
        <div class="flex items-center gap-2 cursor-pointer -m-1 p-1 rounded hover:bg-primary-300">
          <mdi-filter-outline class="h-5 w-5"/>
          Filtre
        </div>
        <div class="flex items-center gap-2 cursor-pointer -m-1 p-1 rounded hover:bg-primary-300">
          <mdi-search class="h-5 w-5"/>
          Recherche
        </div>
      </div>

      <hr class="border border-t-primary-400 my-4"/>

      <Tree v-model="storageSidebar.board_selected" :items="workspaceItems" :show-root="false">
        <template #icon(board)>
          <mdi-application-outline class="h-5 w-5"/>
        </template>
      </Tree>
    </div>
  </aside>
</template>

<script lang="ts" setup>
import Select from "@/components/shared/menu/Select.vue";
import SelectItem from "@/components/shared/menu/SelectItem.vue";

import useAppLocalStorage from "@use/useAppLocalStorage";
import { breakpointsTailwind } from "@vueuse/core";
import { WorkspaceInfo_Children } from "back_types";
import { computed, Ref, watch } from "vue";

const route = useRoute();
const breakpoints = useBreakpoints(breakpointsTailwind);
const isMobile = breakpoints.smaller("lg");
const opened = ref(false);
const { storageSidebar } = useAppLocalStorage();
const workspaces = ref([]);
const selectedWorkspace: Ref<WorkspaceInfo_Children> = ref(null);
const drawer_sidebar = ref();

onClickOutside(drawer_sidebar, () => opened.value = false);

const fetchWorkspaces = async () => {
  const { result } = await api.getWorkspacesByCurrentUser();
  workspaces.value = result;
};

const selectedWorkspaceName = computed(() => {
  const workspace = workspaces.value?.find(value => value.id === storageSidebar.value.workspace_selected);

  if (workspace == null) {
    storageSidebar.value.workspace_selected = null;
    return null;
  }

  return workspace?.name;
});

const workspaceItems = computed(() => {
  const items: AppTreeItem = { label: "root", value: null, children: [] };

  if (selectedWorkspace.value != null) {
    selectedWorkspace.value.boards.forEach(board => {
      items.children.push({ label: board.name, value: board.id, type: "board" });
    });

    selectedWorkspace.value.folders.forEach(folder => {
      const item: AppTreeItem = { label: folder.name, value: null, type: "folder", children: [] };
      if (folder.boards?.length) {
        folder.boards.forEach(board => {
          item.children.push({ label: board.name, value: board.id, type: "board" });
        });
      }
      items.children.push(item);
    });
  }

  return items;
});

await fetchWorkspaces();

const isSidebarClosed = computed(() => {
  return !opened.value;
});

watch(() => storageSidebar.value.workspace_selected,
  async (value: number) => {
    if (value != null) {
      const { result } = await api.getWorkspaceById(value);
      selectedWorkspace.value = result;
    }
  },
  { immediate: true },
);

watch(isMobile,
  (_isMobile) => {
    if (_isMobile) {
      opened.value = false;
    }
  },
);

const toggle = () => {
  opened.value = !opened.value;
};
</script>

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

      <template v-if="storageSidebar.workspace_selected != null">
        <div class="grid grid-cols-1 text-primary-600 mt-4 gap-3">
          <Popover #default="{open}" class="inline-block">
            <Float :offset="6" :placement="isMobile ? null : 'right-start'" enter="transition ease-out duration-100" enter-from="transform opacity-0 scale-95" enter-to="transform opacity-100 scale-100" flip leave="transition ease-in duration-75" leave-from="transform opacity-100 scale-100" leave-to="transform opacity-0 scale-95" portal>
              <PopoverButton as="template">
                <div class="flex w-full items-center gap-2 cursor-pointer -m-1 p-1 rounded hover:bg-primary-300">
                  <mdi-plus class="h-5 w-5"/>
                  Ajouter
                </div>
              </PopoverButton>

              <PopoverPanel #default="{close}">
                <div class="rounded bg-white text-primary-500 border border-primary-200 shadow-3xl">
                  <div class="grid grid-cols-1 gap-3 p-4 min-w-[250px] max-h-[450px] overflow-y-auto scrollbar scrollbar-thin scrollbar-thumb-gray-600 scrollbar-track-gray-300">
                    <div class="flex gap-3 text-primary-600 cursor-pointer px-2 py-1 -mx-2 -my-1 rounded hover:bg-primary-200" @click="showDialogNewView(close)">
                      <mdi-application-outline class="h-5 w-5"></mdi-application-outline>
                      <div>Nouveau tableau</div>
                    </div>
                    <div class="flex gap-3 text-primary-600 cursor-pointer px-2 py-1 -mx-2 -my-1 rounded hover:bg-primary-200" @click="close">
                      <mdi-folder-outline class="h-5 w-5"></mdi-folder-outline>
                      <div>Nouveau dossier</div>
                    </div>
                  </div>
                </div>
              </PopoverPanel>
            </Float>
          </Popover>
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
      </template>
    </div>
  </aside>
</template>

<script lang="ts" setup>
import DialogNewView from "@/components/dialogs/DialogNewView.vue";
import Select from "@/components/shared/menu/Select.vue";
import SelectItem from "@/components/shared/menu/SelectItem.vue";
import { Float } from "@headlessui-float/vue";
import { Popover, PopoverButton, PopoverPanel } from "@headlessui/vue";

import useAppLocalStorage from "@use/useAppLocalStorage";
import { breakpointsTailwind } from "@vueuse/core";
import { WorkspaceInfo_Children } from "back_types";
import { computed, Ref, watch } from "vue";

const { dialog } = useAegir();
const route = useRoute();
const breakpoints = useBreakpoints(breakpointsTailwind);
const isMobile = breakpoints.smaller("md");
const opened = ref(false);
const { storageSidebar } = useAppLocalStorage();
const workspaces = ref([]);
const selectedWorkspace: Ref<WorkspaceInfo_Children> = ref(null);
const drawer_sidebar = ref();

onClickOutside(drawer_sidebar, () => {
  if (isMobile.value) {
    opened.value = false;
  }
});

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

const showDialogNewView = close => {
  if (storageSidebar.value.workspace_selected != null) {
    dialog.create({
      component: DialogNewView,
      props: {
        boardId: storageSidebar.value.workspace_selected,
      },
    });
    close();
  }
};

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

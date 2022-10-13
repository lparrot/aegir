<template>
  <WorkspaceSidebar>
    <template v-if="storageSidebar.workspace_selected != null">
      <div class="grid grid-cols-1 text-primary-600 mt-4 gap-3">

        <Overlay :float-options="{placement: 'right-start'}">
          <template #activator>
            <div class="flex items-center gap-2 cursor-pointer -m-1 p-1 rounded hover:bg-primary-300">
              <mdi-plus class="h-5 w-5"/>
              Ajouter
            </div>
          </template>

          <template #default="{close}">
            <div class="overlay-item" @click="showDialogNewBoard(close)">
              <mdi-application-outline class="h-5 w-5"></mdi-application-outline>
              <div>Nouveau tableau</div>
            </div>
            <div class="overlay-item" @click="showDialogNewFolder(close)">
              <mdi-folder-outline class="h-5 w-5"></mdi-folder-outline>
              <div>Nouveau dossier</div>
            </div>
          </template>
        </Overlay>

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
        <template #type(board)="{item, hover, select}">
          <div class="flex w-full justify-between">
            <div class="flex flex-grow items-center gap-2" @click="select">
              <mdi-application-outline class="h-5 w-5"/>
              <div class="truncate">{{ item.label }}</div>
            </div>

            <Overlay :float-options="{placement: 'right-start'}">
              <template #activator="{open}">
                <div>
                  <mdi-dots-horizontal v-if="hover || open" class="w-5 h-5"/>
                </div>
              </template>

              <div class="overlay-item" @click="deleteBoard(item)">
                <mdi-delete-outline class="h-5 w-5"/>
                Supprimer
              </div>
            </Overlay>
          </div>
        </template>

        <template #type(folder)="{item, hover, select, toggle}">
          <div class="flex w-full justify-between">
            <div class="flex flex-grow items-center gap-2" @click="toggle">
              <div class="truncate">{{ item.label }}</div>
            </div>

            <div v-if="hover">
              <mdi-dots-horizontal class="w-5 h-5"/>
            </div>
          </div>
        </template>
      </Tree>
    </template>
  </WorkspaceSidebar>
</template>

<script lang="ts" setup>
import DialogNewBoard from "@/components/dialogs/DialogNewBoard.vue";
import DialogNewFolder from "@/components/dialogs/DialogNewFolder.vue";
import Overlay from "@/components/shared/overlay/Overlay.vue";
import WorkspaceSidebar from "@/components/WorkspaceSidebar.vue";
import useWorkspaceSidebar from "@use/useWorkspaceSidebar";
import { computed } from "vue";

const { dialog } = useAegir();
const { storageSidebar } = useAppLocalStorage();
const { selectedWorkspace, fetchWorkspaceDetail } = useWorkspaceSidebar({ with_watch: true });

const workspaceItems = computed(() => {
  const items: AppTreeItem = { label: "root", value: null, children: [] };

  if (selectedWorkspace.value != null) {
    selectedWorkspace.value.boards.forEach(board => {
      items.children.push({ label: board.name, value: board.id, type: "board" });
    });

    selectedWorkspace.value.folders.forEach(folder => {
      const item: AppTreeItem = { label: folder.name, value: folder.id, type: "folder", children: [] };
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

const showDialogNewBoard = close => {
  if (storageSidebar.value.workspace_selected != null) {
    dialog.create({
      component: DialogNewBoard,
      props: {
        workspaceId: storageSidebar.value.workspace_selected,
      },
      async onOk() {
        await fetchWorkspaceDetail();
      },
    });
    close();
  }
};

const showDialogNewFolder = close => {
  if (storageSidebar.value.workspace_selected != null) {
    dialog.create({
      component: DialogNewFolder,
      props: {
        workspaceId: storageSidebar.value.workspace_selected,
      },
      async onOk() {
        await fetchWorkspaceDetail();
      },
    });
    close();
  }
};

const deleteBoard = async (item) => {
  dialog.create({
    message: "Etes vous sûr de vouloir supprimer ce tableau ?",
    async onOk() {
      const { success } = await api.deleteBoard(item.value, { cascade: true });
      if (success) {
        await fetchWorkspaceDetail();
      }
    },
  });
};
</script>

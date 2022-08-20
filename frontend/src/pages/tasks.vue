<template>
  <q-page v-if="projectStore.selectedItem" padding>
    <div class="text-subtitle2">
      <div class="row items-center q-gutter-sm">
        <q-icon v-bind="itemInfo"></q-icon>
        <q-breadcrumbs active-color="blue" class="cursor-pointer text-blue-10 ellipsis">
          <q-breadcrumbs-el v-for="hierarchy in projectStore.selectedItem.itemHierarchy" :key="hierarchy.id" :label="hierarchy.name" tag="div" @click="storageSidebar.item_selected = hierarchy.id"/>
        </q-breadcrumbs>
      </div>
      <div class="q-gutter-xs q-mt-xs">
        <template v-if="projectStore.selectedItem.type === 'WORKSPACE'">
          <q-btn color="positive" dense icon="add" size="sm" unelevated @click="showDialogCreateList">Créer une liste</q-btn>
        </template>
        <q-btn color="negative" dense icon="delete" size="sm" unelevated @click="deleteProjectItem">Supprimer</q-btn>
      </div>
    </div>

    <q-separator class="q-my-sm"></q-separator>

    <div v-for="taskByItem in tasks" :key="taskByItem.master?.id">
      <q-card bordered flat>
        <q-card-section>
          <q-expansion-item default-opened dense switch-toggle-side>
            <template v-slot:header>
              <div class="text-h6">{{ taskByItem.master.name }}</div>
            </template>

            <task-by-status v-for="taskByStatus in taskByItem.details" :key="taskByStatus.master?.id" :status="taskByStatus.master" :tasks="taskByStatus.details"></task-by-status>
          </q-expansion-item>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script lang="ts" setup>
import { useProjectStore } from "stores/project";
import { computed, ref, watch } from "vue";
import { api } from "boot/axios";
import { EnumProjectItemType, GroupByMapper, ProjectItemInfo, TaskInfo, TaskStatusInfo } from "app/.generated/rest";
import TaskByStatus from "components/TaskByStatus.vue";
import { Dialog, Notify } from "quasar";
import DialogCreateList from "../components/dialogs/DialogCreateList.vue";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import { useRouter } from "vue-router";

const { storageSidebar } = useAppLocalStorage();
const projectStore = useProjectStore();
const router = useRouter();
const tasks = ref<GroupByMapper<ProjectItemInfo, GroupByMapper<TaskStatusInfo, TaskInfo>>[]>();

const fetchTasks = async (projectItemId) => {
  const { result, success } = await api.getTasksByProjectItemId(projectItemId);
  if (success) {
    tasks.value = result;
  }
};

const showDialogCreateList = () => {
  Dialog.create({
    component: DialogCreateList,
  })
    .onOk(async (params) => {
      const { success, result } = await api.createProjectItem({
        name: params.name,
        type: EnumProjectItemType.VIEW,
        projectId: projectStore.selectedProject.id,
        parentId: projectStore.selectedItem.id,
      });

      if (success) {
        storageSidebar.value.item_selected = result.id;
        await router.push({ name: "tasks" });
        await projectStore.fetchSelectedProject();
      }
    });
};

const deleteProjectItem = () => {
  Dialog.create({
    message: itemInfo.value.deleteMessage,
    cancel: true,
    persistent: true,
  })
    .onOk(async () => {
      const { success } = await api.deleteProjectItem(projectStore.selectedItem.id);

      if (success) {
        storageSidebar.value.item_selected = projectStore.selectedItem.parentId;
        await projectStore.fetchSelectedProject();
        Notify.create({
          message: "Item supprimé",
          color: "positive",
        });
      }
    });
};

watch(
  () => projectStore.selectedItem,
  async (selectedItem) => {
    if (selectedItem != null) {
      await fetchTasks(selectedItem?.id);
    }
  },
  { immediate: true },
);

const itemInfo = computed(() => {
  switch (projectStore.selectedItem?.type) {
    case "WORKSPACE":
      return {
        name: "workspaces",
        color: "blue",
        deleteMessage: "Etes vous sûr de vouloir effectuer la suppression de cet espace ? Cette action supprimera l'espace ainsi que tous les dossiers, les listes et tâches qui le composent",
      };
    case "FOLDER":
      return {
        name: "folder",
        color: "orange",
        deleteMessage: "Etes vous sûr de vouloir effectuer la suppression de ce dosser ? Cette action supprimera le dossier ainsi que toutes les listes et tâches qui le composent",
      };
    case "VIEW":
      return {
        name: "view_sidebar",
        color: "green",
        deleteMessage: "Etes vous sûr de vouloir effectuer la suppression de cette liste ? Cette action supprimera la liste ainsi que toutes les tâches qui le composent",
      };
    default:
      return {};
  }
});
</script>

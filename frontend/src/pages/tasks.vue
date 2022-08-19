<template>
  <q-page v-if="projectStore.selectedItem" padding>
    <div class="text-subtitle2">
      <div class="row items-center q-gutter-sm">
        <q-icon v-bind="itemIcon"></q-icon>
        <q-breadcrumbs active-color="blue" class="text-blue-10 ellipsis">
          <q-breadcrumbs-el v-for="hierarchy in projectStore.selectedItem.itemHierarchy" :key="hierarchy" :label="hierarchy" tag="div"/>
        </q-breadcrumbs>
      </div>
      <div v-if="projectStore.selectedItem.type === 'WORKSPACE'">
        <q-btn color="positive" flat size="sm" @click="showDialogCreateList">Créer une liste</q-btn>
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
import { Dialog } from "quasar";
import DialogCreateList from "../components/dialogs/DialogCreateList.vue";

const projectStore = useProjectStore();
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
      const { success } = await api.createProjectItem({
        name: params.name,
        type: EnumProjectItemType.VIEW,
        projectId: projectStore.selectedProject.id,
        parentId: projectStore.selectedItem.id,
      });

      if (success) {
        await projectStore.fetchSelectedProject();
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

const itemIcon = computed(() => {
  switch (projectStore.selectedItem?.type) {
    case "WORKSPACE":
      return { name: "workspaces", color: "blue" };
    case "FOLDER":
      return { name: "folder", color: "orange" };
    case "VIEW":
      return { name: "view_sidebar", color: "green" };
    default:
      return {};
  }
});
</script>

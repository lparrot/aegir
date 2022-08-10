<template>
  <q-page v-if="projectStore.selectedItem" padding>
    <div class="text-subtitle2">
      <div class="row items-center q-gutter-sm">
        <q-icon v-bind="itemIcon"></q-icon>
        <q-breadcrumbs active-color="blue" class="text-blue-10 ellipsis">
          <q-breadcrumbs-el v-for="hierarchy in projectStore.selectedItem.itemHierarchy" :key="hierarchy" :label="hierarchy" tag="div"/>
        </q-breadcrumbs>
      </div>
    </div>
  </q-page>
</template>

<script lang="ts" setup>
import { useProjectStore } from "stores/project";
import { computed, ref, watch } from "vue";
import { api } from "boot/axios";

const projectStore = useProjectStore();
const tasks = ref();

const fetchTasks = async (projectItemId) => {
  tasks.value = await api.getTasksByProjectItemId(projectItemId);
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

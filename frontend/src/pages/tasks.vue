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
import { computed } from "vue";

const projectStore = useProjectStore();

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
console.log(projectStore.selectedItem);
</script>

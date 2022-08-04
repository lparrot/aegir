<template>
  <q-tree v-if="projectStore.selectedProject != null" ref="itemsTree" v-model:expanded="storageSidebar.items_expanded" :nodes="projectStore.selectedProject?.items" :selected="storageSidebar.item_selected" accordion dense label-key="name" no-connectors no-selection-unset node-key="id" selected-color="grey-2" @update:expanded="onTreeItemExpanded" @update:selected="onTreeItemSelected">
    <template v-slot:default-header="prop">
      <q-tooltip anchor="center right" self="center left">{{ prop.node.name }}</q-tooltip>
      <div :class="{['bg-' + prop.tree?.selectedColor]: prop.key === storageSidebar.item_selected}" class="row q-gutter-x-xs no-wrap items-center full-width q-py-xs q-px-xs rounded-borders">
        <q-icon class="block" size="16px" v-bind="getProjectItemIconProps(prop.node?.type)"/>
        <div class="text-primary ellipsis">
          <span>{{ prop.node.name }}</span>
        </div>
      </div>
    </template>
  </q-tree>
</template>

<script lang="ts" setup>
import { useProjectStore } from "stores/project";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import { useRouter } from "vue-router";

const emit = defineEmits<{
  (e: "item-selected"): void
}>();

////////////////
// Composables
////////////////
const router = useRouter();
const projectStore = useProjectStore();
const { storageSidebar } = useAppLocalStorage();

const getProjectItemIconProps = (type) => {
  switch (type) {
    case "WORKSPACE":
      return { name: "workspaces", color: "blue" };
    case "FOLDER":
      return { name: "folder", color: "orange" };
    case "VIEW":
      return { name: "view_sidebar", color: "green" };
    default:
      return {};
  }
};

const onTreeItemExpanded = (value) => {
  storageSidebar.value.items_expanded = value;
};

const onTreeItemSelected = async (value) => {
  storageSidebar.value.item_selected = value;
  await router.push({ name: "tasks" });
  emit("item-selected");
};
</script>

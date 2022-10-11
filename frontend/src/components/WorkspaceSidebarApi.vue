<template>
  <WorkspaceSidebar>
    <div v-for="table in tables" :key="table.name">
      {{ table.name }}
    </div>
  </WorkspaceSidebar>
</template>

<script lang="ts" setup>
import useAppLocalStorage from "@use/useAppLocalStorage";

const tables = ref([]);

const { storageSidebar } = useAppLocalStorage();

const fetchTables = async () => {
  if (storageSidebar.value.workspace_selected) {
    const { result } = await api.getTables(storageSidebar.value.workspace_selected);
    tables.value = result;
  }
};

await fetchTables();
</script>

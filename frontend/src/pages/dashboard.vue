<template>
  <q-page v-if="projectStore.selectedItem" padding>
    {{ projectStore.selectedItem }}
  </q-page>
</template>

<script lang="ts" setup>
import { useProjectStore } from "stores/project";

const projectStore = useProjectStore();
</script>

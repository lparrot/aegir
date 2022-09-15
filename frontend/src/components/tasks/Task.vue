<template>
  <tr class="bg-primary-50">
    <td v-for="column in columns" :key="column.id" class="h-0">
      <div :class="column.rowClass" :style="{width: column.componentWidth}" class="flex h-full">
        <component :is="column.component" :task="task" class="flex items-center"></component>
      </div>
    </td>
  </tr>
</template>

<script lang="ts" setup>

import { useTaskColumns } from "@use/useTaskColumns";

const props = defineProps({
  task: {},
});

const { columns } = useTaskColumns();
</script>

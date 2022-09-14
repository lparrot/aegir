<template>
  <div class="flex shadow-md bg-primary-50 py-2 px-2">
    <div v-for="column in columns" :key="column.id" :class="column.headerClass" :style="column.headerStyle" class="flex items-center h-full">
      <component :is="column.component" :task="taskModel"></component>
    </div>
  </div>
</template>

<script lang="ts" setup>

import { useTaskColumns } from "@use/useTaskColumns";

const props = defineProps({
  task: {},
});

const emit = defineEmits<{
  (e: "update:task"): any
}>();

const taskModel = useVModel(props, "task", emit);

const { columns } = useTaskColumns();
</script>

<script lang="ts">
export default {
  name: "Task",
};
</script>

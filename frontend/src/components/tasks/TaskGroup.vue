<template>
  <div class="flex flex-col gap-1">
    <div class="flex items-center shadow-md bg-primary-50 py-2 px-2">
      <div v-for="column in columns" :key="column.id" :class="column.headerClass" :style="column.headerStyle" class="flex justify-center h-5 text-primary-400">
        {{ column.header }}
      </div>
    </div>
    <slot></slot>
  </div>
</template>

<script lang="ts" setup>
import { useTaskColumns } from "@use/useTaskColumns";

const { columns } = useTaskColumns();
</script>

<script lang="ts">
export default {
  name: "TaskGroup",
};
</script>

<template>
  <div class="overflow-auto rounded shadow-md">
    <table class="table-auto min-w-full">
      <caption></caption>
      <thead>
      <tr class="h-10 bg-primary-200 text-primary-600">
        <th v-for="column in columns" :key="column.id" :class="column.headerClass" :style="{width: column.componentWidth}" class="px-2" scope="col">{{ column.header }}</th>
      </tr>
      </thead>

      <tbody>
      <slot></slot>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts" setup>
import { useTaskColumns } from "@use/useTaskColumns";

const { columns } = useTaskColumns();
</script>

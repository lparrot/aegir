<template>
  <div>
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

    <div class="flex justify-end p-4">
      <Paginator v-model="pagination"/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useTaskColumns } from "@use/useTaskColumns";

interface Props {
  pagination: AppPagination;
}

const props = withDefaults(defineProps<Props>(), {});

const emit = defineEmits<{
  (e: "update:pagination"): any
}>();

const { pagination } = useVModels(props, emit);

const { columns } = useTaskColumns();
</script>

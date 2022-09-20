<template>
  <button @click="modelPagination.page = modelPagination.page + 1">+</button>
</template>

<script lang="ts" setup>
import { useVModel } from "@vueuse/core";
import { withDefaults } from "vue";

interface Props {
  pagination: AppPagination;
}

const props = withDefaults(defineProps<Props>(), {});

const emit = defineEmits<{
  (e: "update:pagination"): any
}>();

const modelPagination = useVModel(props, "pagination", emit, { defaultValue: { size: 15, page: 1, order: { asc: true } } });
</script>

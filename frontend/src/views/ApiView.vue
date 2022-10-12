<template>
  <div>
    <pre>{{ columns }}</pre>
  </div>
</template>

<script lang="ts" setup>
const route = useRoute();
const columns = ref([]);

watch(
  () => route.params?.table,
  async (value) => {
    const val: string = value as string;
    if (value != null && val.trim()?.length > 0) {
      const { success, result } = await api.getColumns(val);
      if (success) {
        columns.value = result;
      }
    }
  },
  { immediate: true },
);
</script>

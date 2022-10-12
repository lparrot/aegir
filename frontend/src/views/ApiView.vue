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
    if (value != null && (<string> value).trim()?.length > 0) {
      const { success, result } = await api.getColumns(<string> value);
      if (success) {
        columns.value = result;
      }
    }
  },
  { immediate: true },
);
</script>

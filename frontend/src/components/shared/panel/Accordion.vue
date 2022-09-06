<template>
  <slot></slot>
</template>

<script lang="ts" setup>
const props = defineProps({
  group: { type: String, default: null },
});

defineEmits<{}>();
</script>

<template>
  <div class="absolute bottom-10 right-10 flex flex-col gap-4">

  </div>
</template>

<script lang="ts" setup>
defineProps({
  placement: { type: String, default: "bottom-left" },
});

const { notifications } = useAegir();
</script>

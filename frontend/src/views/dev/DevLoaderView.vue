<template>
  <div>
    <BaseButton @click="showLoader">Show loader</BaseButton>
  </div>
</template>

<script lang="ts" setup>
const { loader } = useAegir();

const showLoader = () => {
  loader.start();

  setTimeout(() => {
    loader.stop();
  }, 5000);
};
</script>

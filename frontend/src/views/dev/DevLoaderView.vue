<template>
  <div>
    <BaseButton @click="showLoader">Show loader</BaseButton>
  </div>
</template>

<script lang="ts" setup>
import BaseButton from "@/components/shared/form/BaseButton.vue";
import useAegir from "@use/useAegir";

const { loader } = useAegir();

const showLoader = () => {
  loader.start();

  setTimeout(() => {
    loader.stop();
  }, 5000);
};
</script>

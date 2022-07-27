<template>
  <Suspense>
    <router-view/>

    <template #fallback>
      Chargement ...
    </template>
  </Suspense>
</template>

<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  name: "App",
});
</script>

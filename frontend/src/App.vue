<template>
  <Suspense>
    <router-view/>

    <template #fallback>
      Chargement ...
    </template>
  </Suspense>
</template>

<script lang="ts">

</script>

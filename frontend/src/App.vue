<template>
  <Suspense>
    <RouterView/>

    <template #fallback>
      <div>Chargement ...</div>
    </template>
  </Suspense>
</template>

<script lang="ts" setup>

</script>

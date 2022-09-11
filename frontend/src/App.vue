<template>
  <Suspense>
    <div>
      <RouterView/>
      <ToastGroup/>
      <Loader/>
    </div>

    <template #fallback>
      <div>Chargement ...</div>
    </template>
  </Suspense>
</template>

<script lang="ts" setup>

</script>

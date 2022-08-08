<template>
  <q-page>
    <iframe :src="swaggerPage" class="full-height full-width absolute"></iframe>
  </q-page>
</template>

<script lang="ts" setup>
const swaggerPage = "/swagger-ui/index.html";
</script>

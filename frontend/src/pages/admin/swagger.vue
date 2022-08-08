<template>
  <q-page>
    <iframe :src="swaggerPage" class="full-height full-width absolute no-border"></iframe>
  </q-page>
</template>

<script lang="ts" setup>
const swaggerPage = process.env.NODE_ENV === "production" ? "/swagger-ui/index.html" : `http://localhost:${process.env.DEV_BACKEND_PORT || 8080}/swagger-ui/index.html`;
</script>

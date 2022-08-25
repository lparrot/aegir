<template>
  <q-page>
    <object ref="swaggerContent" :data="swaggerPage" class="full-height full-width absolute" type="text/html">
      L'element 'object' n'est pas géré par votre navigateur
    </object>
  </q-page>
</template>

<script lang="ts" setup>
const swaggerPage = process.env.NODE_ENV === "production" ? "/swagger-ui/index.html" : `http://localhost:${process.env.DEV_BACKEND_PORT || 8080}/swagger-ui/index.html`;
</script>

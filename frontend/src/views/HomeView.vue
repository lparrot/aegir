<template>
  <div class="home">
    <Accordion title="Variables d'environnement">
      <pre>{{ env }}</pre>
    </Accordion>
  </div>
</template>

<script lang="ts" setup>
const env = import.meta.env;
</script>


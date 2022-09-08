<template>
  <div class="home">
    <Accordion title="Variables d'environnement">
      <pre>{{ env }}</pre>
    </Accordion>
  </div>
</template>

<script lang="ts" setup>
import Accordion from "@/components/shared/panel/Accordion.vue";

const env = import.meta.env;
</script>


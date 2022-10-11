<template>
  <div>
    <pre>{{ selectedWorkspace }}</pre>
  </div>
</template>

<script lang="ts" setup>
import useWorkspaceSidebar from "@use/useWorkspaceSidebar";

const { selectedWorkspace } = useWorkspaceSidebar();
</script>

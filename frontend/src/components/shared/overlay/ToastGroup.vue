<template>
  <div class="flex flex-col min-w-lg gap-4">
    <Toast v-for="notification in notifications" :key="notification.id" :notification="notification"></Toast>
  </div>
</template>

<script lang="ts" setup>
const { notifications } = useNotifications();


</script>

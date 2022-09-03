<template>
  <div class="absolute bottom-0 right-0 flex flex-col gap-4 m-10">
    <Toast v-for="notification in notifications" :key="notification.id" :notification="notification" @close="remove(notification)"></Toast>
  </div>
</template>

<script lang="ts" setup>
defineProps({
  placement: { type: String, default: "bottom-left" },
});

const { notifications } = useAegir();

const remove = (notification) => {
  notifications.value = notifications.value.filter(n => n.id !== notification.id);
};
</script>

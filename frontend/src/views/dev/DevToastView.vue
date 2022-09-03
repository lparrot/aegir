<template>
  <div class="grid grid-cols-4 gap-3">
    <BaseButton @click="addNotification('success')">Create success notification</BaseButton>
    <BaseButton @click="addNotification('info')">Create info notification</BaseButton>
    <BaseButton @click="addNotification('warn')">Create warn notification</BaseButton>
    <BaseButton @click="addNotification('danger')">Create danger notification</BaseButton>
  </div>
</template>

<script lang="ts" setup>
const { toast, notifications } = useAegir();

const addNotification = (type: AppNotificationType) => {
  toast.createToast({
    title: "Title of notification",
    message: "New notification !",
    duration: 0,
    type,
  });
};
</script>

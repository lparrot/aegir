<template>
  <BaseButton @click="addNotification">Create notification</BaseButton>

  {{ notifications?.length }}
</template>

<script lang="ts" setup>
const { toast, notifications } = useAegir();

const addNotification = () => {
  toast.createToast({
    title: "Title of notification",
    message: "New notification !",
  });
};
</script>

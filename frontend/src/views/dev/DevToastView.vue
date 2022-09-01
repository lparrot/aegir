<template>
  <BaseButton @click="addNotification">Create notification</BaseButton>


  <ToastGroup></ToastGroup>
</template>

<script lang="ts" setup>
import useNotifications from "@use/useNotifications";

const { notifications, createNotification } = useNotifications();

const addNotification = () => {
  createNotification({
    title: "Title of notification",
    message: "New notification !",
  });
};
</script>

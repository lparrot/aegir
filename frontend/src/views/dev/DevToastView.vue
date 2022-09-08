<template>
  <div class="grid grid-cols-4 gap-3">
    <BaseButton @click="addNotification('success')">Create success notification</BaseButton>
    <BaseButton @click="addNotification('info')">Create info notification</BaseButton>
    <BaseButton @click="addNotification('warn')">Create warn notification</BaseButton>
    <BaseButton @click="addNotification('danger')">Create danger notification</BaseButton>
  </div>

  <div class="mt-5">
    <FieldGroup class="max-w-xs" label="Durée">
      <BaseInput v-model="duration" step="500" type="number"></BaseInput>
    </FieldGroup>
  </div>
</template>

<script lang="ts" setup>
import BaseButton from "@/components/shared/form/BaseButton.vue";
import BaseInput from "@/components/shared/form/BaseInput.vue";
import FieldGroup from "@/components/shared/form/FieldGroup.vue";
import useAegir from "@use/useAegir";
import { ref } from "vue";

const { toast, notifications } = useAegir();

const duration = ref<number>(5000);

const addNotification = (type: AppNotificationType) => {
  toast.createToast({
    title: "Title of notification",
    message: "New notification !",
    duration: duration.value,
    type,
  });
};
</script>

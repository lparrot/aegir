<template>
  <div class="flex flex-col">
    <div class="flex justify-between">
      <div class="font-bold">{{ notification.title }}</div>
      <div class="cursor-pointer rounded-full p-1 hover:bg-primary-200" @click="onClose">
        <XIcon class="h-5 w-5"/>
      </div>
    </div>
    <div class="italic">{{ notification.message }}</div>
  </div>
</template>

<script lang="ts" setup>
import { XIcon } from "@heroicons/vue/solid";
import { defineEmits, PropType } from "vue";

const props = defineProps({
  notification: Object as PropType<AppNotification>,
});

const emits = defineEmits<{
  (e: "close"): void
}>();

onBeforeUnmount(() => {
  console.log("unmounted");
});

const onClose = () => {
  emits("close");
};
</script>

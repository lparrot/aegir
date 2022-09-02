<template>
  <div v-if="show" class="flex w-[300px] px-5 py-4 border rounded-lg bg-white">
    <div class="flex items-start w-full gap-2">
      <div class="flex h-full">
        <CheckCircleIcon class="h-5 w-5 text-success"></CheckCircleIcon>
      </div>
      <div class="flex flex-1 flex-col">
        <div class="flex justify-between items-center">
          <div class="font-bold">{{ notification.title }}</div>
          <div class="cursor-pointer rounded-full p-1 -m-1 hover:bg-primary-200" @click="onClose">
            <XIcon class="h-5 w-5"/>
          </div>
        </div>
        <div class="italic">{{ notification.message }}</div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { CheckCircleIcon } from "@heroicons/vue/outline";
import { XIcon } from "@heroicons/vue/solid";
import { defineEmits, PropType } from "vue";

const props = defineProps({
  notification: Object as PropType<AppNotification>,
});

const emits = defineEmits<{
  (e: "close"): void
}>();

const show = ref(true);

if (props.notification.duration > 0) {
  setTimeout(() => {
    onClose();
  }, props.notification?.duration);
}

onBeforeUnmount(() => {
  console.log("unmounted");
});

const onClose = () => {
  show.value = false;
  emits("close");
};
</script>

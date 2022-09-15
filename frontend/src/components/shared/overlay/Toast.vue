<script lang="ts">
export default {
  name: "Toast",
};
</script>

<template>
  <Transition appear name="flip-in-hor-top">
    <div v-if="show" class="flex flex-col gap-3 w-[300px] px-5 py-4 border rounded-lg bg-white">
      <div class="flex items-start w-full gap-2">
        <div class="flex h-full">
          <component :is="notificationOptions.is" :class="notificationOptions.class" class="h-5 w-5"></component>
        </div>
        <div class="flex flex-1 flex-col">
          <div class="flex justify-between items-center">
            <div class="font-bold">{{ notification.title }}</div>
            <div class="cursor-pointer rounded-full p-1 -m-1 hover:bg-primary-200" @click="onClose">
              <XMarkIcon class="h-5 w-5"/>
            </div>
          </div>
          <div class="italic">{{ notification.message }}</div>
        </div>
      </div>

      <div v-if="notification.duration > 0" :class="notificationOptions.durationClass" :style="{width: `${remaining}%`}" class="h-1.5 rounded-full">

      </div>

    </div>
  </Transition>
</template>

<script lang="ts" setup>
import { CheckCircleIcon, ExclamationTriangleIcon, InformationCircleIcon, NoSymbolIcon } from "@heroicons/vue/24/outline";
import { XMarkIcon } from "@heroicons/vue/24/solid";
import { TransitionPresets, useTransition } from "@vueuse/core";
import { computed, ComputedRef, PropType, ref } from "vue";

const props = defineProps({
  notification: Object as PropType<AppNotification>,
});

const emits = defineEmits<{
  (e: "close"): void
}>();

const show = ref(true);
const baseTimer = ref(100);

const remaining = useTransition(
  baseTimer,
  {
    duration: props.notification?.duration,
    transition: TransitionPresets.linear,
  },
);

if (props.notification?.duration > 0) {
  baseTimer.value = 0;

  setTimeout(() => {
    onClose();
  }, props.notification?.duration);
}

const notificationOptions: ComputedRef<{ class?: string, durationClass?: string, is?: any }> = computed(() => {
  switch (props.notification.type) {
    case "success":
      return { class: "text-success", durationClass: "bg-success", is: CheckCircleIcon };
    case "warn":
      return { class: "text-warn", durationClass: "bg-warn", is: ExclamationTriangleIcon };
    case "danger":
      return { class: "text-danger", durationClass: "bg-danger", is: NoSymbolIcon };
    default:
      return { class: "text-info", durationClass: "bg-info", is: InformationCircleIcon };
  }
});

const onClose = () => {
  show.value = false;
  emits("close");
};
</script>

<template>
  <div class="accordion">
    <div class="flex cursor-pointer w-full justify-between border border-secondary-200 rounded-lg bg-secondary-100 px-4 py-2 text-left text-sm font-medium text-purple-900 hover:bg-secondary-200 focus:outline-none focus-visible:ring focus-visible:ring-secondary-500 focus-visible:ring-opacity-75" role="button" @click="onClickHeader">
      <slot name="title">
        <span>{{ title }}</span>
      </slot>
      <ChevronUpIcon :class="open ? 'rotate-180 transform' : ''" class="h-5 w-5 text-purple-500"/>
    </div>
    <TransitionRoot :show="open" as="template" enter="duration-300 ease-linear" enter-from="opacity-0" enter-to="opacity-100" leave="duration-300 ease-linear" leave-from="opacity-100" leave-to="opacity-0">
      <slot></slot>
    </TransitionRoot>
  </div>
</template>

<script lang="ts" setup>
import { TransitionRoot } from "@headlessui/vue";
import { ChevronUpIcon } from "@heroicons/vue/24/outline";
import { ComponentInternalInstance, getCurrentInstance, inject, Ref } from "vue";

const props = defineProps({
  title: { type: String, default: null },
});

const emit = defineEmits<{
  (e: "open"): void,
  (e: "close"): void,
}>();

const accordionGroup: ComponentInternalInstance = inject("accordion", null);
const open: Ref<boolean> = ref(false);
const index = ref(0);

if (accordionGroup != null) {
  accordionGroup.exposed.accordions.value.push(getCurrentInstance());
  index.value = accordionGroup.exposed.accordions.value.length - 1;
}

const onClickHeader = () => {
  open.value = !open.value;

  if (accordionGroup != null) {
    accordionGroup.exposed.onOpen(index.value);
  }

  if (open.value) {
    emit("open");
  } else {
    emit("close");
  }
};

defineExpose({
  index,
  open,
});
</script>

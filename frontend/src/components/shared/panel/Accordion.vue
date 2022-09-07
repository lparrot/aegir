<template>
  <div :class="['a-accordion--'+accordionColor, $attrs.class]" class="a-accordion" role="button" @click="onClickHeader">
    <slot name="title">
      <span>{{ title }}</span>
    </slot>
    <ChevronUpIcon :class="open ? 'rotate-180 transform' : ''" class="h-5 w-5 text-purple-500"/>
  </div>
  <TransitionRoot :show="open" as="template"
                  enter="transition ease-in-out duration-300 transform"
                  enter-from="-translate-x-full"
                  enter-to="translate-x-0"
                  leave="transition ease-in-out duration-300 transform"
                  leave-from="translate-x-0"
                  leave-to="-translate-x-full">
    <slot></slot>
  </TransitionRoot>
</template>

<script lang="ts" setup>
import { TransitionRoot } from "@headlessui/vue";
import { ChevronUpIcon } from "@heroicons/vue/24/outline";
import { ComponentInternalInstance, getCurrentInstance, inject, Ref } from "vue";

interface Props {
  color?: TailwindColorAndVariant,
  title?: string
}

const props = withDefaults(defineProps<Props>(), {
  color: null,
  title: null,
});

const emit = defineEmits<{
  (e: "open"): void,
  (e: "close"): void,
}>();

const accordionGroup: ComponentInternalInstance = inject("accordion", null);
const open: Ref<boolean> = ref(false);
const index = ref(0);
const accordionColor = ref(props.color);

if (accordionGroup != null) {
  if (props.color == null) {
    accordionColor.value = accordionGroup.exposed.color;
  }
  accordionGroup.exposed.accordions.value.push(getCurrentInstance());
  index.value = accordionGroup.exposed.accordions.value.length - 1;
}

if (accordionColor.value == null) {
  accordionColor.value = "primary";
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

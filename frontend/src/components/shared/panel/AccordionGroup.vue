<template>
  <div class="accordion-group">
    <slot></slot>
  </div>
</template>

<script lang="ts" setup>
import { ComponentInternalInstance, defineProps, getCurrentInstance, provide, Ref } from "vue";

interface Props {
  color: TailwindColorAndVariant,
  single?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  color: "primary",
  single: false,
});

const accordions: Ref<ComponentInternalInstance[]> = ref([]);

provide("accordion", getCurrentInstance());

const onOpen = index => {
  if (props.single) {
    accordions.value.filter(accordion => accordion.exposed.index !== index).forEach(accordion => accordion.exposed.open = false);
  }
};

defineExpose({
  accordions,
  color: props.color,
  onOpen,
});
</script>

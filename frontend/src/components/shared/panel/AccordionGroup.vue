<template>
  <div class="accordion-group">
    <slot></slot>
  </div>
</template>

<script lang="ts" setup>
import { ComponentInternalInstance, getCurrentInstance, provide, Ref } from "vue";

const accordions: Ref<ComponentInternalInstance[]> = ref([]);

provide("accordion", getCurrentInstance());

const onOpen = index => {
  accordions.value.filter(accordion => accordion.exposed.index !== index).forEach(accordion => accordion.exposed.open = false);
};

defineExpose({
  accordions,
  onOpen,
});
</script>

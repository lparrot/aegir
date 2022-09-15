<template>
  <div :class="[`a-badge--${color}`]" class="a-badge">
    <component :is="icon" v-if="icon" class="h-5 w-5"></component>
    <slot></slot>
  </div>
</template>

<script lang="ts" setup>
interface Props {
  color: TailwindColorAndVariant,
  icon: any;
}

const props = withDefaults(defineProps<Props>(), {
  color: "primary",
});
</script>

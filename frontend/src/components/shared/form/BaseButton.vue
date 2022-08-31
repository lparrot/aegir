<template>
  <component :is="tag" :class="[`a-btn--${color}`, {'a-btn-outlined': outlined}]">
    <slot></slot>
  </component>
</template>

<script lang="ts" setup>
interface Props {
  color?: string;
  hoverDarken?: boolean;
  outlined?: boolean;
  tag?: string;
}

const props = withDefaults(defineProps<Props>(), {
  color: "primary",
  tag: "button",
});

const buttonAttrs = computed(() => {
  let _: unknown, attrs: Pick<any, string | number>;

  ({ class: _, ...attrs } = getCurrentInstance().attrs);

  return attrs;
});
</script>

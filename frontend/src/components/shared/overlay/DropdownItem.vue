<script lang="ts">
export default {
  name: "DropdownItem",
};
</script>

<template>
  <RouterLink v-if="isRouter" #default="{navigate}" :to="to" custom>
    <MenuItem :as="tag" class="dropdown-item" v-bind="$attrs" @click="navigate">
      <slot></slot>
    </MenuItem>
  </RouterLink>

  <MenuItem v-else as="tag" class="dropdown-item" v-bind="$attrs">
    <slot></slot>
  </MenuItem>
</template>

<script lang="ts" setup>
import { MenuItem } from "@headlessui/vue";
import { computed, getCurrentInstance } from "vue";
import type { RouteLocationRaw, RouterLinkProps } from "vue-router";

interface Props extends Partial<RouterLinkProps> {
  to?: RouteLocationRaw;
  tag?: string;
}

const props = withDefaults(defineProps<Props>(), {
  tag: "div",
});

const vue = getCurrentInstance();

const isRouter = computed(() => {
  return props.to != null;
});
</script>

<style lang="scss">
.dropdown-item {
  @apply flex gap-2 cursor-pointer hover:bg-primary-100 text-gray-700 block px-4 py-2 text-sm;
}
</style>

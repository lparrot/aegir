<template>
  <aside ref="drawer_sidebar" :class="{'max-w-[25px]': isSidebarClosed, 'max-w-full': !isSidebarClosed, 'fixed': isMobile}" class="relative flex flex-col flex-shrink-0 bg-primary-200 w-72 text-white transition-all duration-150 ease-in h-screen rounded-r-2xl border border-primary-300 shadow-2xl">
    <div class="absolute top-4 -right-4 rounded-full bg-white h-7 w-7 flex items-center justify-center border border-primary-300 hover:bg-primary-100 cursor-pointer" @click="toggle">
      <mdi-chevron-right v-if="isSidebarClosed" class="h-5 w-5 text-primary"/>
      <mdi-chevron-left v-else class="h-5 w-5 text-primary"/>
    </div>

    <div v-show="!isSidebarClosed" class="p-5 text-primary">
      <slot/>
    </div>
  </aside>
</template>

<script lang="ts" setup>
import { breakpointsTailwind } from "@vueuse/core";
import { computed, watch } from "vue";

const breakpoints = useBreakpoints(breakpointsTailwind);
const isMobile = breakpoints.smaller("md");
const { storageSidebar } = useAppLocalStorage();


const drawer_sidebar = ref();

onClickOutside(drawer_sidebar, () => {
  if (isMobile.value) {
    storageSidebar.value.opened = false;
  }
});

const isSidebarClosed = computed(() => {
  return !storageSidebar.value.opened;
});

watch(isMobile,
  (_isMobile) => {
    if (_isMobile) {
      storageSidebar.value.opened = false;
    }
  },
);

const toggle = () => {
  storageSidebar.value.opened = !storageSidebar.value.opened;
};
</script>

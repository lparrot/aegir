<template>
  <aside v-if="route.meta?.with_workspaces" ref="drawer_sidebar" :class="{'max-w-[25px]': isSidebarClosed, 'max-w-full': !isSidebarClosed, 'fixed': isMobile}" class="relative flex flex-col flex-shrink-0 bg-primary-200 w-72 text-white transition-all duration-150 ease-in h-screen rounded-r-2xl border border-primary-300 shadow-2xl">
    <div class="absolute top-4 -right-4 rounded-full bg-white h-7 w-7 flex items-center justify-center border border-primary-300 hover:bg-primary-100 cursor-pointer" @click="toggle">
      <mdi-chevron-right v-if="isSidebarClosed" class="h-5 w-5 text-primary"/>
      <mdi-chevron-left v-else class="h-5 w-5 text-primary"/>
    </div>

    <div v-show="!isSidebarClosed" class="p-5 text-primary">
      <Listbox v-model="storageSidebar.workspace_selected">
        <div class="relative mt-1">
          <ListboxButton class="relative w-full cursor-default rounded-lg bg-white py-2 pl-3 pr-10 text-left shadow-md focus:outline-none focus-visible:border-indigo-500 focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75 focus-visible:ring-offset-2 focus-visible:ring-offset-orange-300 sm:text-sm">
            <span class="block truncate">{{ selectedWorkspaceName }}</span>
            <span v-if="storageSidebar.workspace_selected == null" class="block truncate text-primary-300">Workspaces ...</span>
            <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
            <mdi-chevron-down aria-hidden="true" class="h-5 w-5 text-gray-400"/>
          </span>
          </ListboxButton>

          <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
            <ListboxOptions class="absolute mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
              <ListboxOption v-for="workspace in workspaces" :key="workspace.id" v-slot="{ active, selected }" :value="workspace.id" as="template">
                <li :class="[active ? 'bg-amber-100 text-amber-900' : 'text-gray-900','relative cursor-default select-none py-2 pl-10 pr-4']">
                  <span :class="[selected ? 'font-medium' : 'font-normal','block truncate']">{{ workspace.name }}</span>
                  <span v-if="selected" class="absolute inset-y-0 left-0 flex items-center pl-3 text-amber-600">
                  <mdi-check aria-hidden="true" class="h-5 w-5"/>
                </span>
                </li>
              </ListboxOption>
            </ListboxOptions>
          </transition>
        </div>
      </Listbox>
    </div>
  </aside>
</template>

<script lang="ts" setup>
import { Listbox, ListboxButton, ListboxOption, ListboxOptions } from "@headlessui/vue";

import useAppLocalStorage from "@use/useAppLocalStorage";
import { breakpointsTailwind, useBreakpoints } from "@vueuse/core";
import { computed, watch } from "vue";

const route = useRoute();
const breakpoints = useBreakpoints(breakpointsTailwind);
const isMobile = breakpoints.smaller("lg");
const opened = ref(false);
const { storageSidebar } = useAppLocalStorage();
const workspaces = ref([]);

const fetchWorkspaces = async () => {
  const { result } = await api.getWorkspacesByCurrentUser();
  workspaces.value = result;
};

const selectedWorkspaceName = computed(() => {
  const selectedWorkspace = workspaces.value?.find(workspace => workspace.id === storageSidebar.value.workspace_selected);

  if (selectedWorkspace == null) {
    storageSidebar.value.workspace_selected = null;
  }

  return selectedWorkspace.name;
});

await fetchWorkspaces();

const isSidebarClosed = computed(() => {
  return !opened.value;
});

watch(isMobile,
  (_isMobile) => {
    if (_isMobile) {
      opened.value = false;
    }
  },
);

const toggle = () => {
  opened.value = !opened.value;
};
</script>

<template>
  <Teleport v-if="show" to="body">
    <div class="absolute top-0 left-0 h-full w-full bg-black/50 backdrop-blur"></div>

    <div class="absolute top-16 left-0 w-full inline-flex justify-center">
      <div ref="panel_search" class="relative w-11/12 lg:!w-6/12">
        <Combobox @update:modelValue="onItemClick">
          <ComboboxInput ref="input_search" class="border-0 border-b border-b-primary-200 bg-white w-full h-14 rounded-t-xl placeholder-primary focus:outline-none focus:ring-0" placeholder="Recherche ..." type="text" @change="search = $event.target.value"></ComboboxInput>

          <transition
            enter-active-class="transition duration-100 ease-out"
            enter-from-class="transform scale-95 opacity-0"
            enter-to-class="transform scale-100 opacity-100"
            leave-active-class="transition duration-75 ease-out"
            leave-from-class="transform scale-100 opacity-100"
            leave-to-class="transform scale-95 opacity-0"
          >
            <ComboboxOptions class="z-10 bg-white rounded-b-xl border-primary max-h-56 overflow-auto">
              <ComboboxOption v-for="(item, itemIndex) in items" :key="itemIndex" #default="{active, selected}" :value="item" as="template">
                <li :class="[active ? 'bg-secondary text-white' : 'text-primary']" class="flex gap-2 block px-4 py-2 text-sm cursor-default select-none hover:bg-secondary hover:text-white focus:outline-none" role="option" tabindex="-1">
                  <div class="w-6">
                    <component :is="item.icon" v-if="item.icon != null" class="h-5 w-5"></component>
                  </div>
                  <span class="truncate">{{ item.label }}</span>
                </li>
              </ComboboxOption>
            </ComboboxOptions>
          </transition>
        </Combobox>
      </div>
    </div>
  </Teleport>
</template>

<script lang="ts" setup>
import { Combobox, ComboboxInput, ComboboxOption, ComboboxOptions } from "@headlessui/vue";
import { LinkIcon } from "@heroicons/vue/24/outline";
import { onClickOutside, useMagicKeys, watchDebounced } from "@vueuse/core";
import { nextTick, Ref } from "vue";
import { useRouter } from "vue-router";

const keys = useMagicKeys();
const router = useRouter();

const show = ref(false);
const panel_search = ref();
const input_search = ref();
const search: Ref<string> = ref();

const items: Ref<AppCommanderItem[]> = ref([]);

onClickOutside(panel_search, () => show.value = false);

watchDebounced(
  search,
  () => {
    items.value = [];
    if (search.value != null && search.value.trim() !== "") {
      items.value.push(...router.getRoutes()
        .filter(route => route.meta.title?.toUpperCase().indexOf(search.value.toUpperCase()) > -1).map(route => {
          return <AppCommanderItem> {
            type: "route",
            icon: LinkIcon,
            label: route.meta.title,
            route,
          };
        }));
    }
  }, { debounce: 500 });

watch(
  keys["Escape"],
  (pressed) => {
    if (show.value && pressed) {
      show.value = false;
    }
  },
);

watch(
  keys["Ctrl+Space"],
  (pressed) => {
    if (pressed) {
      items.value = [];
      search.value = null;
      show.value = true;

      nextTick(() => {
        input_search.value.el.focus();
      });
    }
  },
);

const onItemClick = async (item: AppCommanderItem) => {
  if (item.route != null) {
    await router.push(item.route.path);
  } else if (item.action != null) {
    item.action();
  }
  show.value = false;
};
</script>

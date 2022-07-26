<template>
  <Listbox v-model="model">
    <div class="relative">
      <ListboxButton :class="[errorClasses]" class="border relative cursor-pointer w-full cursor-default rounded-lg bg-white py-2 pl-3 pr-10 text-left focus:outline-none focus-visible:border-secondary-500 focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75 focus-visible:ring-offset-2 focus-visible:ring-offset-secondary-300 sm:text-sm">
        <slot name="placeholder">
          <span v-if="model != null" class="block truncate">{{ placeholder }}</span>
          <span v-else class="block truncate text-primary-400">{{ nullLabel }}</span>
        </slot>
        <span class="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
            <mdi-chevron-down aria-hidden="true" class="h-5 w-5 text-gray-400"/>
          </span>
      </ListboxButton>

      <transition leave-active-class="transition duration-100 ease-in" leave-from-class="opacity-100" leave-to-class="opacity-0">
        <ListboxOptions class="absolute mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
          <slot></slot>
        </ListboxOptions>
      </transition>
    </div>
  </Listbox>
</template>

<script lang="ts" setup>
import { Listbox, ListboxButton, ListboxOptions } from "@headlessui/vue";
import { useVModel } from "@vueuse/core";
import { computed } from "vue";

interface Props {
  error?: boolean;
  modelValue?: any;
  nullLabel?: string;
  placeholder?: string;
}

const props = withDefaults(defineProps<Props>(), {
  error: null,
  nullLabel: "Choose ...",
});

const emit = defineEmits<{
  (e: "update:modelValue"): any
}>();

const model = useVModel(props, "modelValue", emit);

const errorClasses = computed(() => {
  if (props.error === true) {
    return "border-danger-300 focus:ring-danger-800 focus:border-danger-800 bg-danger-50";
  } else if (props.error === false) {
    return "border-success-300 focus:ring-success-800 focus:border-success-800 bg-success-50";
  } else {
    return "focus:ring-primary-800 focus:border-primary-800 border-primary-300";
  }
});
</script>

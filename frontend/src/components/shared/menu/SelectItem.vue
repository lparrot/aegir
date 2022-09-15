<script lang="ts">
export default {
  name: "SelectItem",
};
</script>

<template>
  <ListboxOption #default="{ active, selected }" :value="value" as="template">
    <li :class="[active ? 'bg-secondary-100 text-secondary-900' : 'text-gray-900','relative cursor-pointer select-none py-2 pl-10 pr-4']">
      <slot :active="active" :selected="selected" name="label">
        <span :class="[selected ? 'font-medium' : 'font-normal','block truncate']">{{ label }}</span>
        <div v-if="selected" class="absolute inset-y-0 left-0 flex items-center pl-3 text-secondary-600">
          <mdi-check aria-hidden="true" class="h-5 w-5"/>
        </div>
      </slot>
    </li>
  </ListboxOption>
</template>

<script lang="ts" setup>
import { ListboxOption } from "@headlessui/vue";

interface Props {
  value: any;
  label: string;
}

const props = withDefaults(defineProps<Props>(), {});
</script>

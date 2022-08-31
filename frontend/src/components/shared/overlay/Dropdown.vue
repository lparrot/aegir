<template>
  <Menu #default="{open}" as="div" class="relative inline-block text-left">
    <Float enter="transition ease-out duration-100" enter-from="transform opacity-0 scale-95" enter-to="transform opacity-100 scale-100" flip leave="transition ease-in duration-75" leave-from="transform opacity-100 scale-100" leave-to="transform opacity-0 scale-95" portal v-bind="portalOptions">
      <MenuButton>
        <slot name="button">
          <BaseButton class="flex" outlined>
            <span>{{ label }}</span>
            <ChevronUpIcon v-if="open" class="h-5 w-5"/>
            <ChevronDownIcon v-else class="h-5 w-5"/>
          </BaseButton>
        </slot>
      </MenuButton>

      <MenuItems as="section" class="mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none">
        <div class="py-1">
          <slot></slot>
        </div>
      </MenuItems>
    </Float>
  </Menu>
</template>

<script lang="ts" setup>
import type { FloatProps } from "@/types";
import { Float } from "@headlessui-float/vue";
import { Menu, MenuButton, MenuItems } from "@headlessui/vue";
import { ChevronDownIcon, ChevronUpIcon } from "@heroicons/vue/solid";

interface Props {
  label?: string;
  portalOptions?: FloatProps;
}

withDefaults(defineProps<Props>(), {});
</script>

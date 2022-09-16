<template>
  <Popover #default="{open}">
    <Float :offset="6" enter="transition ease-out duration-100" enter-from="transform opacity-0 scale-95" enter-to="transform opacity-100 scale-100" flip leave="transition ease-in duration-75" leave-from="transform opacity-100 scale-100" leave-to="transform opacity-0 scale-95" placement="bottom" portal v-bind="floatOptions">
      <PopoverButton as="template">
        <slot :open="open" name="activator">
          <div class="flex w-full items-center gap-2 cursor-pointer -m-1 p-1 rounded hover:bg-primary-300">
            <slot :open="open" name="button-content">
              <mdi-dots-horizontal class="h-5 w-5"/>
            </slot>
          </div>
        </slot>
      </PopoverButton>

      <PopoverPanel #default="popoverSlotBind">
        <div class="rounded bg-white text-primary-500 border border-primary-200 shadow-3xl">
          <div class="grid grid-cols-1 gap-3 p-4 min-w-[250px] max-h-[450px] overflow-y-auto scrollbar scrollbar-thin scrollbar-thumb-gray-600 scrollbar-track-gray-300" v-bind="overlayOptions">
            <slot v-bind="popoverSlotBind"></slot>
          </div>
        </div>
      </PopoverPanel>
    </Float>
  </Popover>
</template>

<script lang="ts" setup>
import { Float } from "@headlessui-float/vue";
import { Popover, PopoverButton, PopoverPanel } from "@headlessui/vue";

const props = defineProps({
  floatOptions: { type: Object },
  overlayOptions: { type: Object },
});
</script>

<style lang="scss">
.overlay-item {
  @apply flex gap-3 text-primary-600 cursor-pointer px-2 py-1 -mx-2 -my-1 rounded hover:bg-primary-200;
}
</style>

<template>
  <div class="grid grid-cols-1 text-primary-600 gap-3">
    <TreeItem :item="items" :root="true" :show="showRoot">
      <template v-for="(_, slot) in $slots" #[slot]="scope">
        <slot :name="slot" v-bind="scope"/>
      </template>
    </TreeItem>
  </div>
</template>

<script lang="ts" setup>
import { useVModel } from "@vueuse/core";
import { PropType } from "vue";

const props = defineProps({
  items: { type: Object as PropType<AppTreeItem> },
  modelValue: {},
  showRoot: { type: Boolean, default: true },
});

const emit = defineEmits<{
  (e: "update:modelValue"): any
  (e: "update:openedItems"): any
}>();

const model = useVModel(props, "modelValue", emit);

const tree_items = ref([]);
const openedItems = ref([]);

provide("tree", {
  items: tree_items,
  model,
});
</script>

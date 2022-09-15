<template>
  <div v-show="show">
    <div :class="{'bg-primary-300': selected}" class="flex items-center gap-2 -m-1 p-1 cursor-pointer rounded hover:bg-primary-300" @click="onItemClick">
      <template v-if="item.children?.length">
        <mdi-menu-down v-if="modelOpened" class="h-5 w-5"/>
        <mdi-menu-right v-else class="h-5 w-5"/>
      </template>
      <template v-else-if="item.children != null && !item.children.length">
        <mdi-menu-right class="text-primary-400 h-5 w-5"/>
      </template>
      <div class="flex items-center gap-2">
        <slot v-if="item.type && $slots[`type(${item.type})`] != null" :name="`type(${item.type})`"></slot>
        <template v-else>
          <slot :name="`icon(${item.type})`">
            <component :is="item.icon" class="h-5 w-5"/>
          </slot>
          <div class="truncate">{{ item.label }}</div>
        </template>
      </div>
    </div>
  </div>

  <template v-if="item.children?.length">
    <div v-show="(root && !show) || modelOpened" :class="{[spaceClass]: (root && show) || !root}" class="grid grid-cols-1 text-primary-600 gap-3">
      <TreeItem v-for="child in item.children" :item="child">
        <template v-for="(_, slot) in $slots" #[slot]="scope">
          <slot :name="slot" v-bind="scope"/>
        </template>
      </TreeItem>
    </div>
  </template>
</template>

<script lang="ts" setup>
import { useVModel } from "@vueuse/core";
import isEqual from "lodash/isEqual";
import { getCurrentInstance, PropType } from "vue";

const props = defineProps({
  item: { type: Object as PropType<AppTreeItem> },
  opened: { type: Boolean, default: false },
  root: { type: Boolean, default: false },
  show: { type: Boolean, default: true },
  spaceClass: { type: String, default: "ml-7" },
});

const emit = defineEmits<{
  (e: "update:opened"): any
}>();

const modelOpened = useVModel(props, "opened", emit, { defaultValue: props.item.opened, passive: true });

const tree = inject<any>("tree", null);
provide("tree", tree);

const parent = inject<any>("parent", null);
provide("parent", {
  openAscendant() {
    modelOpened.value = true;
    if (parent != null) {
      parent.openAscendant();
    }
  },
});

tree.items.value.push(getCurrentInstance());

const selected = computed(() => {
  if (tree.model.value == null) {
    return false;
  }
  const equal = isEqual(props.item.value, tree.model.value);
  if (equal) {
    if (parent != null) {
      parent.openAscendant();
    }
  }
  return equal;
});

const onItemClick = () => {
  if (props.item.children != null) {
    modelOpened.value = !modelOpened.value;
  } else {
    tree.model.value = props.item.value;
  }
};
</script>

<script lang="ts">
export default {
  name: "TreeItem",
};
</script>

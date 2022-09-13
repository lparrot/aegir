<template>
  <div>
    <Tree ref="tree" v-model="selected" :items="items" :show-root="true">
      <template #icon(child)>
        <mdi-application-outline class="h-5 w-5"/>
      </template>
    </Tree>

    <pre class="mt-5">{{ selected }}</pre>
  </div>
</template>

<script lang="ts" setup>
import { Ref } from "vue";

const selected = ref();
const items: Ref<AppTreeItem> = ref({
  label: "Home",
  type: "root",
  value: { foo: "bar" },
  children: [
    {
      label: "Child 1",
      type: "root",
      value: { foo: "child1" },
      children: [
        {
          label: "Child 1-1",
          type: "child",
          value: { foo: "child11" },
        },
        {
          label: "Child 1-2",
          type: "child",
          value: { foo: "child12" },
        },
      ],
    },
    {
      label: "Child 2",
      type: "root",
      value: { foo: "child2" },
      children: [
        {
          label: "Child 2-1",
          type: "child",
          value: { foo: "child21" },
        },
      ],
    },
  ],
});
</script>

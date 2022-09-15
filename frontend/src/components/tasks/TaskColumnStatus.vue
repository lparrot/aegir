<template>
  <div :style="{backgroundColor: taskModel.status?.color}" class="flex w-full justify-center text-white cursor-pointer hover:brightness-90">
    <div class="font-bold">{{ taskModel.status?.name }}</div>
  </div>
</template>

<script lang="ts" setup>
import { TaskInfo_Detail } from "back_types";
import { PropType, Ref } from "vue";

const props = defineProps({
  task: { type: Object as PropType<TaskInfo_Detail> },
});

const emit = defineEmits<{
  (e: "update:task"): any
}>();

const taskModel: Ref<TaskInfo_Detail> = useVModel(props, "task", emit);
</script>

<script lang="ts">
export default {
  name: "TaskColumnStatus",
};
</script>

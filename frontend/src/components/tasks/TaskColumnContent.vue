<template>
  <div>{{ taskModel.name }}</div>
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
  name: "TaskColumContent",
};
</script>

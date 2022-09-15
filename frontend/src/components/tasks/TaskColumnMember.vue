<template>
  <div>
    <div v-if="taskModel.assigned == null" class="flex items-center justify-center h-8 w-8 rounded-full text-primary-400 border border-primary-400 bg-primary-100 cursor-pointer hover:brightness-95">
      <mdi-user-circle-outline></mdi-user-circle-outline>
    </div>
    <div v-else class="flex items-center justify-center text-lg rounded-full h-8 w-8 font-bold border-secondary-300 bg-secondary-100 text-secondary-300 cursor-pointer hover:brightness-95">{{ getInitials(taskModel.assigned.username) }}</div>
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

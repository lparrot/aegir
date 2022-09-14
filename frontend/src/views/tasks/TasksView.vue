<template>
  <div class="task-view">
    <div class="text-3xl font-bold">{{ boardDetail.name }}</div>
    <div class="text-lg text-primary-400">{{ boardDetail.description }}</div>
  </div>

  <TaskGroup class="mt-4">
    <template v-for="task in boardDetail.tasks" :key="task.id">
      <Task :task="task"></Task>
    </template>
  </TaskGroup>

  <!--  <pre class="text-xs">{{ boardDetail }}</pre>-->
</template>

<script lang="ts" setup>
import useAppLocalStorage from "@use/useAppLocalStorage";
import { BoardInfo_Detail } from "back_types";
import { Ref } from "vue";

const { storageSidebar } = useAppLocalStorage();
const boardDetail: Ref<BoardInfo_Detail> = ref(null);

watch(
  () => storageSidebar.value.board_selected,
  async (selected) => {
    if (selected != null) {
      const { result } = await api.getBoardById(selected);
      boardDetail.value = result;
    }
  },
  { immediate: true });
</script>

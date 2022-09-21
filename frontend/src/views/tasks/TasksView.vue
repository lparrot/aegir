<template>
  <template v-if="boardDetail">
    <div class="task-view">
      <div class="text-3xl font-bold">{{ boardDetail.name }}</div>
      <div class="text-lg text-primary-400">{{ boardDetail.description }}</div>
    </div>

    <TaskGroup v-model:pagination="pagination" class="mt-4">
      <template v-for="task in boardDetail.tasks" :key="task.id">
        <Task :task="task"></Task>
      </template>
    </TaskGroup>
  </template>

  <template v-else>
    <div>Selectionnez un tableau dans la barre de recherche...</div>
  </template>
</template>

<script lang="ts" setup>
import useAppLocalStorage from "@use/useAppLocalStorage";
import { BoardInfo_Detail } from "back_types";
import { Ref } from "vue";

const { storageSidebar } = useAppLocalStorage();
const boardDetail: Ref<BoardInfo_Detail> = ref(null);
const pagination: Ref<AppPagination> = ref({});

const fetchBoardById = async (id) => {
  if (id != null) {
    const { result } = await api.getBoardById(id);
    boardDetail.value = result;
    pagination.value.count = result.tasks.length;
    pagination.value.totalPage = result.tasks.length;
  }
};

await fetchBoardById(storageSidebar.value.board_selected);

watch(
  () => storageSidebar.value.board_selected,
  async (selected) => {
    if (selected != null) {
      await fetchBoardById(selected);
    }
  });
</script>

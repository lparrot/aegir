<template>
  <pre>{{ boardDetail }}</pre>
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

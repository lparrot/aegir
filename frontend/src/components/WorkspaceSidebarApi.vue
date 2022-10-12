<template>
  <WorkspaceSidebar>
    <base-button class="flex gap-2 w-full my-2" color="secondary" @click="openDialogNewTable(null)">
      <mdi-plus class="h-5 w-5"></mdi-plus>
      <span>Ajouter</span>
    </base-button>
    <template v-for="table in tables" :key="table.name">
      <div :class="{'text-secondary': table.name === route.params?.table }" class="flex gap-2 my-3 font-bold cursor-pointer hover:text-secondary" @click="selectTable(table)">
        <mdi-table></mdi-table>
        <span>{{ table.name }}</span>
      </div>
    </template>
  </WorkspaceSidebar>
</template>

<script lang="ts" setup>
import DialogApiEditTable from "@/components/dialogs/DialogApiEditTable.vue";
import useAppLocalStorage from "@use/useAppLocalStorage";
import { TableDto } from "back_types";

const tables = ref<TableDto[]>([]);

const { dialog } = useAegir();
const { storageSidebar } = useAppLocalStorage();
const router = useRouter();
const route = useRoute();

const fetchTables = async () => {
  if (storageSidebar.value.workspace_selected) {
    const { result } = await api.getTables(storageSidebar.value.workspace_selected);
    tables.value = result;
  }
};

const openDialogNewTable = async (table) => {
  dialog.create({
    component: DialogApiEditTable,
    props: {
      table,
    },
    async onOk() {
      await fetchTables();
    },
  });
};

const selectTable = (table: TableDto) => {
  router.push({ name: router.currentRoute.value.name, params: { table: table.name } });
};

await fetchTables();
</script>

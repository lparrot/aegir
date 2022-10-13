<template>
  <div v-if="selectedTable != null">
    <div class="flex gap-2 mb-3">
      <base-button class="flex" color="primary" @click="newColumn">
        <mdi-plus class="h-5 w-5"/>
        <span>Ajouter une colonne</span>
      </base-button>
    </div>

    <Datatable :fields="fields" :items="columns" :paginate="false">
      <template #cell(actions)="{item}">
        <Dropdown>
          <template #button>
            <mdi-dots-vertical class="h-5 w-5"/>
          </template>

          <DropdownItem @click="removeColumn(item)">
            <mdi-delete class="h-5 w-5 text-danger"/>
            <span>Supprimer</span>
          </DropdownItem>
        </Dropdown>
      </template>

      <template #cell(info)="{item}">
        <div class="flex gap-1 items-center mx-1">
          <mdi-key-variant v-if="item.primaryKey" class="text-orange-400"/>
          <mdi-key-variant v-if="item.foreignKey" class="text-green-800"/>
          <mdi-key-variant v-if="!item.primaryKey && item.uniqueKey" class="text-red-700"/>
        </div>
      </template>
    </Datatable>
  </div>

  <div v-else>
    <p>Selectionnez une table dans la liste</p>
  </div>
</template>

<script lang="ts" setup>
import { MysqlType, TableColumnDto } from "back_types";
import { Ref } from "vue";
import DialogApiEditColumn from "../components/dialogs/DialogApiEditColumn.vue";

const route = useRoute();
const { dialog } = useAegir();
const columns: Ref<TableColumnDto[]> = ref<TableColumnDto[]>([]);
const types: Ref<MysqlType[]> = ref<MysqlType[]>();

const selectedTable = computed(() => {
  const val: string = route.params?.table as string;
  if (route.params?.table != null && val.trim()?.length > 0) {
    return val;
  }
  return null;
});

const fields: DatatableField[] = [
  { key: "actions", field: "actions", label: "", sortable: false },
  { key: "info", field: "info", label: "", sortable: false },
  { key: "name", field: "name", label: "Nom" },
  { key: "type", field: "type", label: "Type" },
  { key: "size", field: "size", label: "Taille", align: "center" },
  { key: "nullable", field: "nullable", label: "Nullable ?", align: "center" },
];

const fetchColumns = async () => {
  const { success, result } = await api.getColumns(selectedTable.value);
  if (success) {
    columns.value = result;
  }
};

const newColumn = async () => {
  dialog.create({
    component: DialogApiEditColumn,
    props: {
      tableName: selectedTable.value,
      types: types.value,
    },
    async onOk() {
      await fetchColumns();
    },
  });
};

const removeColumn = async (column: TableColumnDto) => {
  dialog.create({
    message: "Etes vous sûr de vouloir supprimer la colonne ?",
    showCancel: true,
    async onOk() {
      const { success } = await api.removeColumn(selectedTable.value, column.name);
      if (success) {
        await fetchColumns();
      }
    },
  });
};

watch(
  () => route.params?.table,
  async () => {
    if (selectedTable.value != null) {
      await fetchColumns();
    }
  },
  { immediate: true },
);
</script>

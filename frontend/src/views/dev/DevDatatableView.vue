<template>
  <Datatable :fields="fields" :items="items" selectable @row-click="onRowClick">
    <template #cell(projects)="{value}">
      <span>{{ value.toUpperCase() }}</span>
    </template>
  </Datatable>
</template>

<script lang="ts" setup>
import map from "lodash/map";
import DialogEditUser from "../../components/dialogs/DialogEditUser.vue";

const { dialog } = useAegir();

const items = ref();

const fetchUsers = async () => {
  const { result } = await api.getUsers();
  items.value = result;
};

await fetchUsers();

const fields = ref<DatatableField[]>([
  { key: "username", label: "Nom d'utilisateur" },
  { key: "projects", label: "Projet", fieldName: "projects", transform: value => map(value, o => o.name).join(", ") },
]);

const onRowClick = (user) => {
  dialog.create({
    component: DialogEditUser,
    props: {
      user,
    },
    async onOk() {
      await fetchUsers();
    },
  });
};
</script>

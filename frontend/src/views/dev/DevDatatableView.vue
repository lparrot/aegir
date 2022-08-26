<template>
  <Datatable :fields="fields" :items="items" selectable @row-click="onRowClick">
    <template #cell(projects)="{value}">
      <span>{{ value.toUpperCase() }}</span>
    </template>
  </Datatable>
</template>

<script lang="ts" setup>
import Datatable from "@/components/shared/data/Datatable.vue";
import { ref } from "vue";
import { api } from "@/api";
import map from "lodash/map";
import useAegir from "@/composables/useAegir";
import DialogEditUser from "../../components/dialogs/DialogEditUser.vue";

const { dialog } = useAegir();

const items = ref();

const { result } = await api.getUsers();
items.value = result;

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
  });
};
</script>

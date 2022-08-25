<template>
  <Datatable :fields="fields" :items="items">
    <template #cell(username)="{value}">
      <span>{{ value.toUpperCase() }}</span>
    </template>
  </Datatable>
</template>

<script lang="ts" setup>
import Datatable from "@/components/shared/data/Datatable.vue";
import { ref } from "vue";
import { api } from "@/api";
import map from "lodash/map";
import pick from "lodash/pick";

const items = ref();

const { result } = await api.getUsers();
items.value = result;

const fields = ref<DatatableField[]>([
  { key: "username", label: "Nom d'utilisateur" },
  { key: "project", label: "Projet", fieldName: "projects", transform: value => map(value, o => pick(o, [ "id", "name" ])) },
]);
</script>

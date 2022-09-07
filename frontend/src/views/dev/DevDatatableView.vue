<template>
  <Datatable :fields="fields" :items="items" selectable striped @row-click="onRowClick">
    <template #cell(avatar)="{value}">
      <div class="h-10 w-10">
        <img :src="value" alt="avatar" class="rounded-full">
      </div>
    </template>

    <template #cell(userDataEmail)="{value}">
      <div class="inline-flex items-center gap-2 hover:underline" @click="mailto(value)">
        <EnvelopeIcon class="h-5 w-5"/>
        {{ value }}
      </div>
    </template>

    <template #cell(projects)="{value}">
      <Badge v-if="checkStringNotEmpty(value)" :icon="CheckBadgeIcon" class="font-bold" color="yellow">{{ value.toUpperCase() }}</Badge>
    </template>
  </Datatable>
</template>

<script lang="ts" setup>
import { checkStringNotEmpty } from "@/utils/string";
import { CheckBadgeIcon, EnvelopeIcon } from "@heroicons/vue/24/outline";
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
  { key: "avatar", transform: (_value, item) => `https://picsum.photos/60/60?random=${item.id}` },
  { key: "username", label: "Nom d'utilisateur" },
  { key: "userDataEmail", label: "Email", preventClick: true },
  { key: "profileLabel", label: "Profil" },
  { key: "projects", label: "Projet", fieldName: "projects", transform: value => map(value, o => o.name).join(", "), preventClick: true },
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

const mailto = (email) => {
  window.location.href = "mailto:" + email;
};
</script>

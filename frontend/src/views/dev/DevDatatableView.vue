<template>
  <Datatable ref="datatable" :fields="fields" class="max-h-full" fetch-url="/api/users" selectable striped @row-click="onRowClick">
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

    <template #cell(workspaces)="{value}">
      <Badge v-if="checkStringNotEmpty(value)" :icon="CheckBadgeIcon" class="font-bold" color="yellow">{{ value.toUpperCase() }}</Badge>
    </template>
  </Datatable>
</template>

<script lang="ts" setup>
import Datatable from "@/components/shared/data/Datatable.vue";
import Badge from "@/components/shared/panel/Badge.vue";
import { CheckBadgeIcon, EnvelopeIcon } from "@heroicons/vue/24/outline";
import useAegir from "@use/useAegir";
import map from "lodash/map";
import { ref } from "vue";
import DialogEditUser from "../../components/dialogs/DialogEditUser.vue";

const { dialog } = useAegir();

const fields = ref<DatatableField[]>([
  { key: "avatar", transform: (_value, item) => `https://picsum.photos/60/60?random=${item.id}`, sortable: false },
  { key: "username", label: "Nom d'utilisateur" },
  { key: "userDataEmail", label: "Email", preventClick: true },
  { key: "profileLabel", label: "Profil" },
  { key: "workspaces", label: "Workspaces", field: "workspaces", transform: value => map(value, o => o.name).join(", "), preventClick: true, sortable: false },
]);

const datatable = ref();

const onRowClick = (user) => {
  dialog.create({
    component: DialogEditUser,
    props: {
      user,
    },
    async onOk() {
      await datatable.value.refresh();
    },
  });
};

const mailto = (email) => {
  window.location.href = "mailto:" + email;
};
</script>

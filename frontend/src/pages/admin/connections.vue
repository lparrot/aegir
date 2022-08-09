<template>
  <q-page padding>
    <q-table :columns="fields" :rows="connections">
      <template #body-cell-actions="props">
        <q-td :props="props">
          <template v-if="props.row.username !== authStore.user.sub">
            <q-btn color="negative" dense icon="delete" size="sm" unelevated @click="onRemoveSession(props.row)"></q-btn>
          </template>
        </q-td>
      </template>

      <template #body-cell-connectionDate="props">
        <q-td :props="props">
          {{ $dayjs(props.value, "DD/MM/YYYY HH:mm:ss").from($dayjs(), true) }}
        </q-td>
      </template>
    </q-table>
  </q-page>
</template>

<script lang="ts" setup>
import { onBeforeUnmount, ref } from "vue";
import useWebsocket from "src/composables/useWebsocket";
import { Notify, QTableColumn, useQuasar } from "quasar";
import { api } from "boot/axios";
import { useAuthStore } from "stores/auth";

////////////////
// Composables
////////////////
const socket = useWebsocket();
const authStore = useAuthStore();
const $q = useQuasar();

const connections = ref([]);
const fields = ref<QTableColumn[]>([
  { name: "actions", field: "actions", label: "" },
  { name: "username", field: "username", label: "Nom d'utilisateur", align: "left" },
  { name: "lastname", field: "lastname", label: "Nom", align: "left" },
  { name: "firstname", field: "firstname", label: "Prénom", align: "left" },
  { name: "email", field: "email", label: "E-mail", align: "left" },
  { name: "profil", field: "profil", label: "Profil", align: "left" },
  { name: "connectionDate", field: "connectionDate", label: "En ligne depuis", align: "left" },
]);

const fetchConnections = async () => {
  const { success, result } = await api.getWebsockets();

  if (success) {
    connections.value = result;
  }
};

const onRemoveSession = async (session) => {
  $q.dialog({
    message: "Etes vous sûr de vouloir fermer la session selectionnée ?",
    persistent: true,
    cancel: true,
  })
    .onOk(async payload => {
      const { success } = await api.deleteWebsocketsByUsername({ username: session.username });
      if (success) {
        Notify.create({
          message: `Session fermée`,
          color: "positive",
        });
      }
    });
};

const subscription = await socket.subscribe("/topic/session", async message => {
  await fetchConnections();
});

onBeforeUnmount(async () => {
  subscription.unsubscribe();
});

await fetchConnections();
</script>

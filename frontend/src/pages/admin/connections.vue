<template>
  <q-page padding>
    <q-table :columns="fields" :rows="connections">
      <template #body-cell-connectionDate="props">
        <q-td :props="props">
          {{ $dayjs(props.value, "DD/MM/YYYY HH:mm:ss").from($dayjs(), true) }}
        </q-td>
      </template>
    </q-table>
  </q-page>
</template>

<script lang="ts" setup>
import useWebsocketRepository from "src/composables/repositories/useWebsocketRepository";
import { onBeforeUnmount, ref } from "vue";
import useWebsocket from "src/composables/useWebsocket";
import { QTableColumn } from "quasar";

////////////////
// Composables
////////////////
const socket = useWebsocket();
const websocketRepository = useWebsocketRepository();

const connections = ref([]);
const fields = ref<QTableColumn[]>([
  { name: "username", field: "username", label: "Nom d'utilisateur", align: "left" },
  { name: "lastname", field: "lastname", label: "Nom", align: "left" },
  { name: "firstname", field: "firstname", label: "Prénom", align: "left" },
  { name: "email", field: "email", label: "E-mail", align: "left" },
  { name: "profil", field: "profil", label: "Profil", align: "left" },
  { name: "connectionDate", field: "connectionDate", label: "En ligne depuis", align: "left" },
]);

const fetchConnections = async () => {
  const { success, result } = await websocketRepository.getConnections();

  if (success) {
    connections.value = result;
  }
};

await fetchConnections();

const subscription = await socket.subscribe("/topic/session", async message => {
  await fetchConnections();
});

onBeforeUnmount(async () => {
  subscription.unsubscribe();
});
</script>

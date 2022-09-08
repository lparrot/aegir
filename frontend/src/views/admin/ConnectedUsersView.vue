<template>
  <Datatable :fields="fields" :items="connections">

  </Datatable>
</template>

<script lang="ts" setup>
const socket = useWebsocket();
const authStore = useAuthStore();
const { dialog, toast } = useAegir();

const connections = ref([]);

const fields = ref<DatatableField[]>([
  { key: "actions", label: "" },
  { key: "username", label: "Nom d'utilisateur" },
  { key: "lastname", label: "Nom" },
  { key: "firstname", label: "Prénom" },
  { key: "email", label: "E-mail" },
  { key: "profil", label: "Profil" },
  { key: "connectionDate", label: "En ligne depuis" },
]);

const fetchConnections = async () => {
  const { success, result } = await api.getWebsockets();

  if (success) {
    connections.value = result;
  }
};

const onRemoveSession = async (session) => {
  dialog.create({
    message: "Etes vous sûr de vouloir fermer la session selectionnée ?",
    backdropDismiss: false,
    labelOk: "Oui",
    labelCancel: "Non",
    async onOk(payload) {
      const { success } = await api.deleteWebsocketsByUsername({ username: session.username });
      if (success) {
        toast.createToast({
          message: `Session fermée`,
          type: "success",
        });
      }
    },
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

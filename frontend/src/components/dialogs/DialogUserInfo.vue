<template>
  <q-dialog ref="dialogRef" full-width v-bind="dialogOptions">
    <template v-if="loaded">
      <q-card :style="{minWidth: $q.screen.lt.sm ? '100vw': '400px'}" class="column">
        <q-card-section class="flex no-wrap items-center q-py-sm">
          <div class="flex no-wrap items-center q-gutter-xs text-h6">
            <q-avatar color="primary" size="sm" text-color="white">{{ getInitials(user.username) }}</q-avatar>
            <div>{{ user.userDataFullname }}</div>
          </div>
          <q-space/>
          <q-btn v-close-popup flat icon="close" round></q-btn>
        </q-card-section>

        <q-card-section class="col">
          <div class="text-grey-7 text-caption"><span class="text-weight-bold">Profil:</span> {{ user.profileLabel }}</div>
        </q-card-section>

        <q-space></q-space>

        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" unelevated>Fermer</q-btn>
        </q-card-actions>
      </q-card>
    </template>
  </q-dialog>
</template>

<script lang="ts" setup>
import { QDialogOptions, useDialogPluginComponent } from "quasar";
import { onBeforeMount, ref } from "vue";
import { api } from "boot/axios";
import { UserDto } from "app/.generated/rest";
import { getInitials } from "src/utils/string.utils";

interface Props {
  dialogOptions: QDialogOptions;
  userId: number;
}

const props = defineProps<Props>();

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const { dialogRef, onDialogHide, onDialogOK, onDialogCancel } = useDialogPluginComponent();

const loaded = ref(false);
const user = ref<UserDto>();

const fetchUser = async () => {
  const { success, result } = await api.getUserById(props.userId);
  if (success) {
    user.value = result;
  }
};

onBeforeMount(async () => {
  // async methods
  await fetchUser();

  loaded.value = true;
});
</script>

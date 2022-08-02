<template>
  <q-dialog ref="dialogRef">
    <q-card>
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h5">Erreur...</div>
        <q-space/>
        <q-btn v-close-popup flat icon="close" round></q-btn>
      </q-card-section>

      <q-card-section>
        {{ error.message }}
      </q-card-section>

      <q-card-actions align="right">
        <q-btn v-close-popup color="primary" unelevated>Fermer</q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script lang="ts" setup>
import { useDialogPluginComponent } from "quasar";
import { AxiosError } from "axios";

interface Props {
  error: AxiosError;
}

const props = defineProps<Props>();

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const { dialogRef, onDialogHide, onDialogOK, onDialogCancel } = useDialogPluginComponent();
</script>

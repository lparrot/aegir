<template>
  <q-dialog ref="dialogRef" maximized>
    <q-card class="column">
      <q-card-section class="row items-center q-py-sm bg-red-1 text-red-9">
        <div class="text-h4">Erreur...</div>
        <q-space/>
        <q-btn v-close-popup flat icon="close" round></q-btn>
      </q-card-section>

      <q-card-section>
        <div class="text-h6 text-red-9">{{ data?.message }}</div>
      </q-card-section>

      <q-card-section v-if="data?.detail?.trace" class="col text-caption">
        <q-scroll-area class="full-height full-width" visible>
          <div v-for="(trace, traceIndex) in data.detail.trace" :key="`trace-${traceIndex}`">{{ trace }}</div>
        </q-scroll-area>
      </q-card-section>

      <q-space></q-space>

      <q-card-actions align="right">
        <q-btn v-close-popup color="primary" unelevated>Fermer</q-btn>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script lang="ts" setup>
import { useDialogPluginComponent } from "quasar";
import { AxiosError } from "axios";
import { computed } from "vue";

interface Props {
  error: AxiosError;
}

const props = defineProps<Props>();

const data = computed(() => {
  return props.error?.response?.data;
});

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const { dialogRef, onDialogHide, onDialogOK, onDialogCancel } = useDialogPluginComponent();
</script>

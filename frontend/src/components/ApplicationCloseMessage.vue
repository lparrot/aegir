<template>
  <q-dialog ref="dialogRef" persistent>
    <q-card bordered flat>
      <q-card-section>
        <div class="column items-center">
          <q-spinner-gears color="negative" size="xl"></q-spinner-gears>
          <div class="text-h6">Application en cours de fermeture</div>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script lang="ts" setup>
import { useDialogPluginComponent } from "quasar";

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const { dialogRef } = useDialogPluginComponent();
</script>

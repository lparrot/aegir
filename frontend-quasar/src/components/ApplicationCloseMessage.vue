<template>
  <q-dialog ref="dialogRef" persistent>
    <q-card bordered flat>
      <q-card-section>
        <div class="column items-center">
          <q-spinner-bars color="negative" size="xl"></q-spinner-bars>
          <div class="text-h6">Application injoignable</div>
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

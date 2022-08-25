<template>
  <q-dialog ref="dialogRef" maximized>
    <template v-if="loaded">
      <q-card class="column">
        <q-card-section class="row items-center q-py-sm">
          <div class="text-h6">TITRE</div>
          <q-space/>
          <q-btn v-close-popup flat icon="close" round></q-btn>
        </q-card-section>

        <q-card-section class="col scroll">
          <!-- -->
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
import { useDialogPluginComponent } from "quasar";
import { onBeforeMount, ref } from "vue";

interface Props {

}

const props = defineProps<Props>();

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const { dialogRef, onDialogHide, onDialogOK, onDialogCancel } = useDialogPluginComponent();

const loaded = ref(false);

onBeforeMount(async () => {
  // async methods

  loaded.value = true;
});
</script>

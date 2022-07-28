<template>
  <q-dialog ref="dialogRef" maximized persistent @hide="onDialogHide">
    <q-card class="column">
      <q-card-section class="row items-center q-pb-none">
        <q-space/>
        <q-btn v-close-popup dense flat icon="close" round/>
      </q-card-section>

      <q-card-section class="col">
        <q-stepper ref="stepper" v-model="step" alternative-labels animated contracted flat>

          <q-step :done="step > 1" :name="1" icon="settings" title="">
            <span class="row justify-center text-h6">Créer un espace</span>
            <q-separator class="q-my-sm"></q-separator>
            <q-input :model-value="formData.name" dense filled label="Nom de l'espace"></q-input>
          </q-step>

          <q-step :done="step > 2" :name="2" icon="create_new_folder" title="">
            <span class="row justify-center text-h6">Quels status de tâche souhaitez-vous ?</span>
          </q-step>

          <q-step :name="3" icon="add_comment" title="">
            <span class="row justify-center text-h6">Paramètres par défaut pour les vues</span>
          </q-step>
        </q-stepper>
      </q-card-section>

      <q-space/>

      <q-card-actions align="right">
        <q-btn v-if="step > 1" class="q-ml-sm" color="primary" flat label="Retour" @click="$refs.stepper.previous()"/>
        <q-btn v-if="step < 3" class="q-ml-sm" color="primary" label="Continuer" @click="$refs.stepper.next()"/>
        <q-btn v-if="step === 3" color="primary" label="Terminer" @click="onSubmit"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { useDialogPluginComponent } from "quasar";

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const { dialogRef, onDialogHide, onDialogOK, onDialogCancel } = useDialogPluginComponent();
const step = ref(1);

const formData = ref({ name: null });

const onSubmit = () => {
  console.log("on submit");
  onDialogOK();
};
</script>

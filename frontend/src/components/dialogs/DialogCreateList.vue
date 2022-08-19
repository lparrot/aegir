<template>
  <q-dialog ref="dialogRef">
    <template v-if="loaded">
      <q-card class="column" style="max-width: 95vw; width: 50vh">
        <q-card-section class="row items-center q-py-sm">
          <div class="text-h6">Créer une liste</div>
          <q-space/>
          <q-btn v-close-popup flat icon="close" round></q-btn>
        </q-card-section>

        <q-card-section class="col scroll">
          <Form id="formCreate" as="form" @submit="onSubmit">
            <Field #default="{ errorMessage, value, field }" label="nom de la liste" name="name" rules="required">
              <label class="q-field__label" for="form_name">Nom de la liste</label>
              <q-input id="form_name" :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense outlined type="text" v-bind="field"/>
            </Field>
          </Form>
        </q-card-section>

        <q-space></q-space>

        <q-card-actions align="right">
          <q-btn v-close-popup color="primary" form="formCreate" unelevated>Fermer</q-btn>
          <q-btn form="formCreate" type="submit" unelevated>Créer la liste</q-btn>
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

const onSubmit = async (values, actions) => {
  onDialogOK(values);
};
</script>

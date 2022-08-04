<template>
  <q-dialog ref="dialogRef" maximized persistent @hide="onDialogHide">
    <q-card class="column">
      <q-card-section class="row items-center q-pb-none">
        <q-space/>
        <q-btn v-close-popup dense flat icon="close" round/>
      </q-card-section>

      <q-card-section class="col">
        <q-stepper ref="stepper" v-model="step" active-color="blue" alternative-labels contracted done-color="positive" error-color="negative" flat keep-alive @before-transition="onTransition">

          <q-step :done="step > 1" :error="stepErrors.step1" :name="1" icon="settings" title="">
            <span class="row justify-center text-h6">Créer un espace</span>
            <q-separator class="q-my-sm"></q-separator>
            <Form ref="step1" :initial-values="formData">
              <Field #default="{ errorMessage, value, field }" label="nom de l'espace" name="name" rules="required">
                <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" dense filled label="Nom de l'espace" v-bind="field"></q-input>
              </Field>
            </Form>
          </q-step>

          <q-step :done="step > 2" :error="stepErrors.step2" :name="2" icon="create_new_folder" title="">
            <span class="row justify-center text-h6">Quels status de tâche souhaitez-vous ?</span>
            <q-separator class="q-my-sm"></q-separator>
            <Form ref="step2" :initial-values="formData">

            </Form>
          </q-step>

          <q-step :error="stepErrors.step3" :name="3" icon="add_comment" title="">
            <span class="row justify-center text-h6">Paramètres par défaut pour les vues</span>
            <q-separator class="q-my-sm"></q-separator>
            <Form ref="step3" :initial-values="formData">

            </Form>
          </q-step>

        </q-stepper>
      </q-card-section>

      <q-space/>

      <q-card-actions align="right">
        <q-btn v-if="step > 1" class="q-ml-sm" color="primary" flat label="Retour" unelevated @click="stepper.previous()"/>
        <q-btn v-if="step < 3" class="q-ml-sm" color="primary" label="Continuer" unelevated @click="stepper.next()"/>
        <q-btn v-if="step === 3" :disable="hasError" color="primary" label="Terminer" unelevated @click="onSubmit"/>
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script lang="ts" setup>
import { computed, reactive, Ref, ref } from "vue";
import { QStep, QStepper, useDialogPluginComponent } from "quasar";
import { Form, FormContext } from "vee-validate";

defineEmits([
  ...useDialogPluginComponent.emits,
]);

const step1 = ref<FormContext>();
const step2 = ref<FormContext>();
const step3 = ref<FormContext>();

const stepErrors = reactive({ step1: false, step2: false, step3: false });

const { dialogRef, onDialogHide, onDialogOK } = useDialogPluginComponent();
const stepper = ref<QStepper>();
const step = ref(1);

const formData = ref({ name: null });

const hasError = computed(() => {
  return stepErrors.step1 || stepErrors.step2 || stepErrors.step3;
});

const onTransition = async (newStep, oldStep) => {
  if (oldStep === newStep) {
    return;
  }

  async function checkStep(step: Ref<FormContext>, stepErrorAttr: string): Promise<void> {
    if (step.value != null) {
      const { valid } = await step.value.validate();
      stepErrors[stepErrorAttr] = !valid;
      if (!valid) {
        stepper.value.previous();
      }
    }
  }

  await checkStep(step1, "step1");
  await checkStep(step2, "step2");
  await checkStep(step3, "step3");

};

const onSubmit = async () => {
  onDialogOK();
};
</script>

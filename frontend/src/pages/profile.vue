<template>
  <q-page padding>
    <Form :initial-values="user" as="form" @submit="onSubmit">
      <q-card bordered flat>
        <q-card-section>
          <div class="text-h6">Modification de profil</div>
          <div class="text-subtitle2">Modifiez les informations ci-dessous</div>
        </q-card-section>

        <q-card-section class="row q-col-gutter-sm">
          <q-input :model-value="user?.username" aria-autocomplete="both" autocomplete class="col-12 col-md-6 q-mb-sm" dense disable label="Nom d'utilisateur" outlined type="text"/>

          <Field #default="{ errorMessage, value, field }" as="div" class="col-12 col-md-6" label="adresse email" name="userData.email" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense label="Adresse email" outlined type="text" v-bind="field"/>
          </Field>
          <Field #default="{ errorMessage, value, field }" as="div" class="col-12 col-md-6" label="nom" name="userData.lastname" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense label="Nom" outlined type="text" v-bind="field"/>
          </Field>
          <Field #default="{ errorMessage, value, field }" as="div" class="col-12 col-md-6" label="prénom" name="userData.firstname" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense label="Prénom" outlined type="text" v-bind="field"/>
          </Field>
          <Field #default="{ errorMessage, value, field }" as="div" class="col-12" label="adresse" name="userData.address" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense label="Adresse postal" outlined type="text" v-bind="field"/>
          </Field>
          <Field #default="{ errorMessage, value, field }" as="div" class="col-12 col-md-6" label="code postal" name="userData.postalCode" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense label="Code postal" outlined type="text" v-bind="field"/>
          </Field>
          <Field #default="{ errorMessage, value, field }" as="div" class="col-12 col-md-6" label="ville" name="userData.city" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense label="Ville" outlined type="text" v-bind="field"/>
          </Field>
          <Field #default="{ errorMessage, value, field }" as="div" class="col-12" label="à mon sujet" name="userData.about" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete autogrow class="q-mb-sm" dense label="A mon sujet ..." outlined type="text" v-bind="field"/>
          </Field>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn color="primary" type="submit" unelevated>Modifier les informations</q-btn>
        </q-card-actions>
      </q-card>
    </Form>
  </q-page>
</template>

<script lang="ts" setup>
import { onMounted, Ref, ref } from "vue";
import { FormActions } from "vee-validate";
import { api } from "boot/axios";
import { UserInfo } from "app/.generated/rest";
import { Notify } from "quasar";

const user: Ref<UserInfo> = ref<UserInfo>();

onMounted(async () => {
  const { success, result } = await api.getAuthUserData();
  if (success) {
    user.value = result;
  }
});

const onSubmit = async (values: UserInfo, actions: FormActions<any>) => {
  const { success } = await api.putAuthUserData(user.value.id, values.userData);

  if (success) {
    Notify.create({
      message: "Modification effectuée",
      color: "positive",
    });
  }
};
</script>

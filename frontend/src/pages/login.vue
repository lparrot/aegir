<template>
  <q-page padding>
    <Form :initial-values="initialValues" class="row justify-center" @submit="onSubmit">
      <q-card class="col-12 col-md-6">
        <q-card-section>
          <div class="text-h6">Connexion</div>
          <div class="text-subtitle2">Connexion à l'application</div>
        </q-card-section>

        <q-card-section>
          <Field #default="{ errorMessage, value, field }" label="nom d'utilisateur" name="username" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete class="q-mb-sm" dense label="Nom d'utilisateur" outlined type="text" v-bind="field"/>
          </Field>

          <Field #default="{ errorMessage, value, field }" label="mot de passe" name="password" rules="required">
            <q-input :error="!!errorMessage" :error-message="errorMessage" :model-value="value" aria-autocomplete="both" autocomplete dense label="Mot de passe" outlined type="password" v-bind="field"/>
          </Field>
        </q-card-section>

        <q-separator></q-separator>

        <q-card-actions align="right">
          <q-btn color="primary" label="Se connecter" type="submit"></q-btn>
        </q-card-actions>
      </q-card>
    </Form>
  </q-page>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { useAuthStore } from "stores/auth";

const authStore = useAuthStore();
const initialValues = reactive({
  username: null,
  password: null,
});
const onSubmit = async (values) => {
  await authStore.login(values);
};
</script>

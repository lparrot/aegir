<template>
  <q-page padding>
    <template v-if="authStore.isLoggedIn">
      <p class="text-h6">Vous êtes déjà connecté.</p>

      <p>Pour vous déconnecter, merci de cliquer sur votre nom d'utilisateur en haut à droite, puis de selectionner le menu <code class="text-red bg-grey-3">Deconnexion</code></p>

      <p>Pour aller sur la page d'accueil, merci de cliquer
        <router-link :to="{name: 'index'}">ici</router-link>
      </p>
    </template>

    <template v-else>
      <Form :initial-values="initialValues" class="row justify-center" @submit="onSubmit">
        <q-card class="col-12 col-md-8">
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
            <q-btn color="primary" label="Se connecter" type="submit" unelevated></q-btn>
          </q-card-actions>
        </q-card>
      </Form>
    </template>
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

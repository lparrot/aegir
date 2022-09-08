<template>
  <section class="flex justify-center">
    <form class="grid lg:w-1/2 gap-4" @submit.prevent="handleSubmit">

      <FieldGroup #default="{error}" :validation-field="v$.username" label="Nom d'utilisateur:">
        <BaseInput v-model="v$.username.$model" :error="error" autocomplete/>
      </FieldGroup>

      <FieldGroup #default="{error}" :validation-field="v$.password" label="Mot de passe:">
        <BaseInput v-model="v$.password.$model" :error="error" autocomplete type="password"/>
      </FieldGroup>

      <BaseButton color="secondary" type="submit">
        Se connecter
      </BaseButton>
    </form>
  </section>
</template>

<script lang="ts" setup>
import { useAuthStore } from "@/stores/auth";
import useAegir from "@use/useAegir";
import useVuelidate from "@vuelidate/core";
import { required } from "@vuelidate/validators";
import { ParamsSecurityLogin } from "back_types";
import { ref } from "vue";
import { useRouter } from "vue-router";

/* COMPOSABLES */
const authStore = useAuthStore();
const router = useRouter();
const { toast } = useAegir();

/* DATA */
const form = ref<ParamsSecurityLogin>({});

const rules = {
  username: { required },
  password: { required },
};

const v$ = useVuelidate<ParamsSecurityLogin>(rules, form);

/* METHODS */
const handleSubmit = async () => {
  const valid = await v$.value.$validate();

  if (valid) {
    const user = await authStore.login({ username: form.value.username, password: form.value.password });

    if (user != null) {
      await router.push({ name: "home" });
      toast.createToast({
        message: `Vous êtes maintenant connecté sous : ${ form.value.username }`,
        title: "Connexion",
        type: "success",
      });
    }
  }
};
</script>

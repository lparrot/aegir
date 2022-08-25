<template>
  <section class="flex justify-center">
    <form class="grid lg:w-1/2 gap-4" @submit.prevent="handleSubmit">

      <FormGroup #default="{error}" :validation-field="v$.username" label="Nom d'utilisateur:">
        <BaseInput v-model="v$.username.$model" :error="error" autocomplete/>
      </FormGroup>

      <FormGroup #default="{error}" :validation-field="v$.password" label="Mot de passe:">
        <BaseInput v-model="v$.password.$model" :error="error" autocomplete type="password"/>
      </FormGroup>

      <BaseButton color="secondary" type="submit">
        Se connecter
      </BaseButton>
    </form>
  </section>
</template>

<script lang="ts" setup>
import { reactive } from "vue";
import { useAuthStore } from "@/stores/auth";
import BaseInput from "@/components/shared/form/BaseInput.vue";
import FormGroup from "@/components/shared/form/FormGroup.vue";
import BaseButton from "@/components/shared/form/BaseButton.vue";
import { useRouter } from "vue-router";
import { required } from "@vuelidate/validators";
import useVuelidate from "@vuelidate/core";

/* COMPOSABLES */
const authStore = useAuthStore();
const router = useRouter();

/* DATA */
const form = reactive({
  username: "",
  password: "",
});

const rules = {
  username: { required },
  password: { required },
};

const v$ = useVuelidate(rules, form);

/* METHODS */
const handleSubmit = async () => {
  const valid = await v$.value.$validate();

  if (valid) {
    const user = await authStore.login({ username: form.username, password: form.password });

    if (user != null) {
      console.log("auth success ", user);
      await router.push({ name: "home" });
    }
  }
};
</script>

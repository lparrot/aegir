<template>
  <Modal ref="dialogRef" panel-classes="w-full lg:!w-10/12" @ok="onOk">
    <template #title>
      <div>Modification de {{ user.userDataFullname }}</div>
    </template>

    <div class="grid gap-2 grid-flow-row-dense grid-cols-1 lg:!grid-cols-2">
      <FieldGroup label="Nom d'utilisateur:">
        <BaseInput v-model="form.username"></BaseInput>
      </FieldGroup>

      <FieldGroup label="E-mail:">
        <BaseInput v-model="form.email"></BaseInput>
      </FieldGroup>

      <FieldGroup label="Nom:">
        <BaseInput v-model="form.lastname"></BaseInput>
      </FieldGroup>

      <FieldGroup label="Prénom:">
        <BaseInput v-model="form.firstname"></BaseInput>
      </FieldGroup>

      <FieldGroup class="lg:col-span-2" label="Adresse:">
        <BaseInput v-model="form.address"></BaseInput>
      </FieldGroup>

      <FieldGroup label="Code postal">
        <BaseInput v-model="form.postalCode"></BaseInput>
      </FieldGroup>

      <FieldGroup label="Ville">
        <BaseInput v-model="form.city"></BaseInput>
      </FieldGroup>

      <FieldGroup class="lg:col-span-2" label="A votre sujet...">
        <BaseTextarea v-model="form.about" rows="5"></BaseTextarea>
      </FieldGroup>
    </div>
  </Modal>
</template>

<script lang="ts" setup>
import { api } from "@/api";
import BaseInput from "@/components/shared/form/BaseInput.vue";
import BaseTextarea from "@/components/shared/form/BaseTextarea.vue";
import FieldGroup from "@/components/shared/form/FieldGroup.vue";
import Modal from "@/components/shared/overlay/Modal.vue";
import useDialog from "@/composables/useDialog";
import useVuelidate from "@vuelidate/core";
import { email, required } from "@vuelidate/validators";
import { ParamsUserEdit, UserDto } from "back_types";
import { ref } from "vue";

interface Props {
  user?: UserDto,
}

const props = defineProps<Props>();

defineEmits([
  ...useDialog.emits,
]);

const form = ref<ParamsUserEdit>();

if (props.user == null) {
  form.value = {};
} else {
  form.value = {
    username: props.user.username,
    email: props.user.userDataEmail,
    lastname: props.user.userDataLastname,
    firstname: props.user.userDataFirstname,
    address: props.user.userDataAddress,
    postalCode: props.user.userDataPostalCode,
    city: props.user.userDataCity,
    about: props.user.userDataAbout,
  };
}

const rules = {
  username: { required },
  email: { required, email },
  lastname: { required },
  firstname: { required },
  address: {},
  postalCode: {},
  city: {},
  about: {},
};

const v$ = useVuelidate<ParamsUserEdit>(rules, form);

const { dialogRef, onDialogOk, onDialogCancel, onDialogHide, onDialogClose } = useDialog();

const onOk = async () => {
  if (props.user != null) {
    const valid = await v$.value.$validate();

    if (!valid) return;

    const { success } = await api.updateUser(props.user.id, form.value);

    if (success) {
      onDialogOk();
    }
  }
};
</script>

<template>
  <Modal ref="dialogRef" panel-classes="w-full md:!w-6/12" title="Tableau" @ok="onOk">
    <div class="grid grid-cols-1 gap-4">
      <FieldGroup #default="{error}" :validation-field="v$.name" label="Nom du dossier" required>
        <BaseInput v-model="form.name" :error="error" placeholder="Nouveau dossier"></BaseInput>
      </FieldGroup>
    </div>
  </Modal>
</template>

<script lang="ts" setup>
import useDialog from "@use/useDialog";
import useVuelidate from "@vuelidate/core";
import { required } from "@vuelidate/validators";
import { ParamsCreateFolder } from "back_types";
import { Ref } from "vue";

interface Props {
  workspaceId: number;
}

const props = withDefaults(defineProps<Props>(), {});

const emit = defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk, onDialogCancel, onDialogHide, onDialogClose } = useDialog();

const form: Ref<ParamsCreateFolder> = ref<ParamsCreateFolder>({
  name: null,
});

const rules = {
  name: { required },
};

const v$ = useVuelidate<ParamsCreateFolder>(rules, form);

const onOk = async () => {
  const valid = await v$.value.$validate();

  if (valid) {
    const { success } = await api.createFolder(props.workspaceId, form.value);

    if (success) {
      onDialogOk();
    }
  }
};
</script>

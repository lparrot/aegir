<template>
  <Modal ref="dialogRef" label-ok="Créer" panel-classes="w-full md:!w-6/12" title="Tableau" @ok="onOk">
    <div class="grid grid-cols-1 gap-4">
      <FieldGroup #default="{error}" :validation-field="v$.name" label="Nom du tableau" required>
        <BaseInput v-model="form.name" :error="error" placeholder="Nouveau tableau"></BaseInput>
      </FieldGroup>
      <FieldGroup #default="{error}" :validation-field="v$.description" label="Description du tableau" required>
        <BaseTextarea v-model="form.description" :error="error" placeholder="Ce tableau permet ..." rows="6"></BaseTextarea>
      </FieldGroup>
    </div>
  </Modal>
</template>

<script lang="ts" setup>
import useDialog from "@use/useDialog";
import useVuelidate from "@vuelidate/core";
import { required } from "@vuelidate/validators";
import { ParamsCreateBoard } from "back_types";
import { Ref } from "vue";

interface Props {
  workspaceId: number;
}

const props = withDefaults(defineProps<Props>(), {});

const emit = defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk } = useDialog();

const form: Ref<ParamsCreateBoard> = ref<ParamsCreateBoard>({
  name: null,
  description: null,
});

const rules = {
  name: { required },
  description: { required },
};

const v$ = useVuelidate<ParamsCreateBoard>(rules, form);

const onOk = async () => {
  const valid = await v$.value.$validate();

  if (valid) {
    const { success } = await api.createBoard(props.workspaceId, form.value);

    if (success) {
      onDialogOk();
    }
  }
};
</script>

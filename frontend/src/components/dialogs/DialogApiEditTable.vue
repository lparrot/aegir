<template>
  <Modal ref="dialogRef" :backdrop-dismiss="false" panel-classes="w-11/12 lg:!w-8/12" @ok="onOk">
    <template #title>
      <span>{{ table == null ? `Création d'une table` : `Modification de la table ${table.name}` }}</span>
    </template>

    <div class="grid gap-2 grid-flow-row-dense">
      <FieldGroup #default="{error}" :validation-field="v$.name" label="Nom de la table:">
        <BaseInput v-model="form.name" :error="error"></BaseInput>
      </FieldGroup>

      <FieldGroup #default="{error}" :validation-field="v$.remarks" label="Commentaire:">
        <BaseInput v-model="form.remarks" :error="error"></BaseInput>
      </FieldGroup>
    </div>
  </Modal>
</template>

<script lang="ts" setup>
import Modal from "@/components/shared/overlay/Modal.vue";
import useAppLocalStorage from "@use/useAppLocalStorage";
import useDialog from "@use/useDialog";
import useVuelidate from "@vuelidate/core";
import { maxLength, required } from "@vuelidate/validators";
import { ParamsEditTable, TableDto } from "back_types";
import { Ref } from "vue";

interface Props {
  table: TableDto;
}

const props = withDefaults(defineProps<Props>(), {
  table: null,
});

const emit = defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk } = useDialog();

const { storageSidebar } = useAppLocalStorage();

const rules = {
  name: { required, maxLengthValue: maxLength(40) },
  remarks: { required },
};

const form: Ref<ParamsEditTable> = ref<ParamsEditTable>({
  name: props.table?.name,
  remarks: props.table?.remarks,
});

const v$ = useVuelidate<ParamsEditTable>(rules, form);

const onOk = async () => {
  const valid = await v$.value.$validate();

  if (valid) {
    const { success } = await api.createTable(storageSidebar.value.workspace_selected, form.value);

    if (success) {
      onDialogOk();
    }
  }
};
</script>

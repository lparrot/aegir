<template>
  <Modal ref="dialogRef" :backdrop-dismiss="false" panel-classes="w-11/12 lg:!w-5/12" @ok="onOk">
    <template #title>
      <span>{{ column == null ? `Création d'une colonne` : `Modification de la colonne ${column.name}` }}</span>
    </template>

    <div class="grid gap-2 grid-flow-row-dense">
      <FieldGroup #default="{error}" :validation-field="v$.name" label="Nom de la colonne:">
        <BaseInput v-model="form.name" :error="error"></BaseInput>
      </FieldGroup>

      <FieldGroup #default="{error}" :validation-field="v$.type" label="Type de la colonne:">
        <Select v-model="form.type" :error="error" :placeholder="form.type" class="mt-4" null-label="Choisissez...">
          <SelectItem v-for="type in dataStore.mysql_types" :key="type" :label="type" :value="type"/>
        </Select>
      </FieldGroup>

      <FieldGroup #default="{error}" :validation-field="v$.size" label="Taille:">
        <BaseInput v-model="form.size" :error="error" type="number"></BaseInput>
      </FieldGroup>

      <FieldGroup label="Nullable ?:">
        <BaseInput v-model="form.nullable" type="checkbox"></BaseInput>
      </FieldGroup>

      <FieldGroup label="Commentaire:">
        <BaseInput v-model="form.remarks"></BaseInput>
      </FieldGroup>
    </div>
  </Modal>
</template>

<script lang="ts" setup>
import Modal from "@/components/shared/overlay/Modal.vue";
import { useDataStore } from "@/stores/data";
import useDialog from "@use/useDialog";
import useVuelidate from "@vuelidate/core";
import { maxLength, numeric, required } from "@vuelidate/validators";
import { ParamsEditColumn, ParamsEditTable, TableColumnDto } from "back_types";
import { Ref } from "vue";

interface Props {
  column?: TableColumnDto;
  tableName: string;
}

const props = withDefaults(defineProps<Props>(), {
  column: null,
});

const emit = defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk } = useDialog();

const dataStore = useDataStore();

const rules = {
  name: { required, maxLengthValue: maxLength(40) },
  type: { required },
  size: { numeric },
};

const form: Ref<ParamsEditColumn> = ref<ParamsEditColumn>({
  name: props.column?.name,
  type: props.column?.type,
  size: props.column?.size || 0,
  nullable: props.column?.nullable,
  remarks: props.column?.remarks,
  tableName: props.tableName,
});

onMounted(async () => {
  await dataStore.fetchMysqlTypes();
});

const v$ = useVuelidate<ParamsEditTable>(rules, form);

const onOk = async () => {
  const valid = await v$.value.$validate();

  if (valid) {
    const { success } = await api.createColumn(form.value);

    if (success) {
      onDialogOk();
    }
  }
};
</script>

<template>
  <Modal ref="dialogRef" label-ok="Créer" panel-classes="w-full md:!w-6/12" title="Tableau" @ok="onOk">
    <FieldGroup label="Nom du tableau" required>
      <BaseInput v-model="form.name" placeholder="Nouveau tableau"></BaseInput>
    </FieldGroup>
  </Modal>
</template>

<script lang="ts" setup>
import useDialog from "@use/useDialog";

interface Props {
  boardId: number;
}

const props = withDefaults(defineProps<Props>(), {});

const emit = defineEmits([
  ...useDialog.emits,
]);

const form = reactive({});

const { dialogRef, onDialogOk, onDialogCancel, onDialogHide, onDialogClose } = useDialog();

const onOk = () => {
  onDialogOk();
};
</script>

<script lang="ts">
export default {
  name: "DialogNewView",
};
</script>

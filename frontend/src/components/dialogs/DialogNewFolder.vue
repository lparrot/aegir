<template>
  <Modal ref="dialogRef" panel-classes="w-full md:!w-6/12" title="Tableau" @ok="onOk">
    <div class="grid grid-cols-1 gap-4">
      <FieldGroup label="Nom du dossier" required>
        <BaseInput v-model="form.name" placeholder="Nouveau dossier"></BaseInput>
      </FieldGroup>
    </div>
  </Modal>
</template>

<script lang="ts" setup>
import useDialog from "@use/useDialog";

interface Props {
  workspaceId: number;
}

const props = withDefaults(defineProps<Props>(), {});

const emit = defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk, onDialogCancel, onDialogHide, onDialogClose } = useDialog();

const form = reactive({
  name: null,
  description: null,
});

const onOk = async () => {
  const { success } = await api.createFolder(props.workspaceId, { name: form.name });

  if (success) {
    onDialogOk();
  }
};
</script>

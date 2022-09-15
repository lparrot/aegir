<template>
  <Modal ref="dialogRef" label-ok="Créer" panel-classes="w-full md:!w-6/12" title="Tableau" @ok="onOk">
    <div class="grid grid-cols-1 gap-4">
      <FieldGroup label="Nom du tableau" required>
        <BaseInput v-model="form.name" placeholder="Nouveau tableau"></BaseInput>
      </FieldGroup>
      <FieldGroup label="Description du tableau" required>
        <BaseTextarea v-model="form.description" placeholder="Ce tableau permet ..." rows="6"></BaseTextarea>
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

const form = reactive({
  name: null,
  description: null,
});

const { dialogRef, onDialogOk, onDialogCancel, onDialogHide, onDialogClose } = useDialog();

const onOk = async () => {
  const { success } = await api.createBoard(props.workspaceId, { name: form.name, description: form.description });

  if (success) {
    onDialogOk();
  }
};
</script>

<script lang="ts">
export default {
  name: "DialogNewBoard",
};
</script>

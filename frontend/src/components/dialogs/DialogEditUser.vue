<template>
  <Modal ref="dialogRef" panel-classes="w-8/12" @ok="onOk">
    <pre>{{ user }}</pre>
  </Modal>
</template>

<script lang="ts" setup>
import Modal from "@/components/shared/overlay/Modal.vue";
import useDialog from "@/composables/useDialog";
import { UserDto } from "back_types";

interface Props {
  user?: UserDto,
}

const props = defineProps<Props>();

defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk, onDialogCancel, onDialogHide, onDialogClose } = useDialog();

const onOk = () => {
  onDialogOk();
};
</script>

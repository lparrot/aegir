<template>
  <Modal ref="dialogRef" :backdrop-dismiss="false" panel-classes="w-11/12 lg:!w-5/12" @ok="onOk">

  </Modal>
</template>

<script lang="ts" setup>
import Modal from "@/components/shared/overlay/Modal.vue";
import useDialog from "@use/useDialog";

const emit = defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk } = useDialog();

const onOk = async () => {
  onDialogOk({});
};
</script>

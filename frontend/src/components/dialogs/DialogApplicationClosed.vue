<script lang="ts">
export default {
  name: "DialogApplicationClosed",
};
</script>

<template>
  <Modal ref="dialogRef" :backdrop-dismiss="false" panel-classes="w-11/12 lg:!w-5/12">
    <div class="flex items-center justify-center h-40">
      <div class="animate-ping">
        <SignalSlashIcon class="h-10 w-10 text-warn"></SignalSlashIcon>
      </div>
    </div>
  </Modal>
</template>

<script lang="ts" setup>
import Modal from "@/components/shared/overlay/Modal.vue";
import { SignalSlashIcon } from "@heroicons/vue/24/outline";
import useDialog from "@use/useDialog";

const emit = defineEmits([
  ...useDialog.emits,
]);

const { dialogRef } = useDialog();
</script>

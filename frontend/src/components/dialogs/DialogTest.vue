<template>
  <Modal ref="dialogRef" description="Description concernant la modale de test" panel-classes="w-full h-full" title="Modale de test" @ok="onOk">
    Lorem ipsum dolor sit amet, consectetur adipisicing elit. Animi corporis esse exercitationem fugit ipsam, iusto neque officiis quas voluptatum! Aspernatur, cum delectus doloribus eos magni minima nam obcaecati qui vero!

    <p class="my-1 font-bold">Affichage des props :</p>

    <pre>{{ propsData }}</pre>
  </Modal>
</template>

<script lang="ts" setup>
import Modal from "@/components/shared/overlay/Modal.vue";
import useDialog from "@/composables/useDialog";

interface Props {
  propsData?: any,
}

const props = defineProps<Props>();

defineEmits([
  ...useDialog.emits,
]);

const { dialogRef, onDialogOk, onDialogCancel, onDialogHide, onDialogClose } = useDialog();

const onOk = () => {
  onDialogOk({
    success: true,
    ...props.propsData,
  });
};
</script>

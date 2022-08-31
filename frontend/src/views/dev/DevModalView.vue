<template>
  <section class="flex gap-2">
    <BaseButton @click="toggleModalDialogTest">
      Toggle modal
    </BaseButton>

    <BaseButton @click="toggleModalDialogClassic">
      Toggle modal classic
    </BaseButton>
  </section>
</template>

<script lang="ts" setup>
import DialogTest from "@/components/dialogs/DialogTest.vue";

const { dialog } = useAegir();

const toggleModalDialogTest = () => {
  dialog.create({
    component: DialogTest,
    props: {
      propsData: {
        foo: "bar",
      },
    },
    onOk(payload) {
      console.log("onOk:", payload);
    },
  });
};

const toggleModalDialogClassic = () => {
  dialog.create({
    message: "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab alias amet architecto at atque consequatur dolor est illum iste laborum minima nulla perspiciatis, placeat provident ratione repudiandae rerum soluta veniam?",
    title: "Modale de test",
    description: "Modale pour tester le service de modal",
    showCancel: false,
  });
};
</script>

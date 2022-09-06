<template>
  <TransitionRoot :show="isOpen" as="template" unmount>
    <Dialog as="div" class="relative z-10" @close="hide">
      <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0" enter-to="opacity-100" leave="duration-200 ease-in" leave-from="opacity-100" leave-to="opacity-0">
        <div aria-hidden="true" class="fixed inset-0 bg-black/30 backdrop-blur"/>
      </TransitionChild>

      <div class="fixed inset-0 overflow-y-auto">
        <div class="flex min-h-full items-center justify-center p-4 text-center">
          <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100" leave="duration-200 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
            <DialogPanel :class="panelClasses" class="flex flex-col transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
              <DialogTitle v-if="$slots.title != null || title != null" as="h3" class="text-xl font-medium leading-6 text-gray-900">
                <div class="flex justify-between">
                  <div>
                    <slot name="title">
                      <span v-if="title">{{ title }}</span>
                    </slot>
                  </div>

                  <div class="p-2 rounded-full hover:bg-primary-100 cursor-pointer duration-300" @click="close">
                    <XMarkIcon class="h-5 w-5"/>
                  </div>
                </div>
              </DialogTitle>

              <DialogDescription v-if="$slots.description != null || description != null" as="h5" class="leading-6 italic text-gray-700">
                <slot name="description">
                  <span v-if="description">{{ description }}</span>
                </slot>
              </DialogDescription>

              <div class="mt-2 h-full">
                <p class="text-sm text-gray-500">
                  <slot></slot>
                </p>
              </div>

              <div class="mt-4 flex gap-1 justify-end">
                <slot name="actions">
                  <BaseButton v-if="showCancel" @click="cancel">{{ labelCancel }}</BaseButton>
                  <BaseButton color="success" @click="ok">{{ labelOk }}</BaseButton>
                </slot>
              </div>
            </DialogPanel>
          </TransitionChild>
        </div>
      </div>
    </Dialog>
  </TransitionRoot>
</template>

<script lang="ts" setup>
import { Dialog, DialogDescription, DialogPanel, DialogTitle, TransitionChild, TransitionRoot } from "@headlessui/vue";
import { XMarkIcon } from "@heroicons/vue/24/solid";
import { Ref } from "vue";

interface Props {
  description?: string;
  labelCancel?: string;
  labelOk?: string;
  modelValue?: boolean;
  panelClasses?: string | string[];
  showCancel?: boolean;
  title?: string;
}

const props = withDefaults(defineProps<Props>(), {
  labelOk: "Ok",
  labelCancel: "Annuler",
  panelClasses: "w-1/2",
  showCancel: true,
});

const emit = defineEmits<{
  (e: "update:modelValue", value: any): void,
  (e: "ok", value?: any): void,
  (e: "cancel", value?: any): void,
  (e: "close"): void,
  (e: "hide"): void,
}>();

const isOpen: Ref<boolean> = ref(props.modelValue);

const changeModelValue = (value) => {
  isOpen.value = value;
  emit("update:modelValue", value);
};

const show = () => {
  changeModelValue(true);
};

const hide = () => {
  changeModelValue(false);
  emit("hide");
};

const ok = () => {
  emit("ok");
  hide();
};

const cancel = () => {
  emit("cancel");
  hide();
};

const close = () => {
  emit("close");
  hide();
};

defineExpose({
  show,
  hide,
  close,
  ...props,
});
</script>

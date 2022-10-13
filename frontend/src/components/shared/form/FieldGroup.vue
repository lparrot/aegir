<template>
  <div class="field-group">
    <label v-if="label != null" :for="idLabel" class="block text-sm font-medium text-gray-700">{{ label }}</label>
    <div ref="slot_container" class="mt-1 rounded-md">
      <slot :error="error"></slot>
    </div>
    <p v-if="hint" class="mt-2 text-sm text-primary-500">{{ hint }}</p>
    <p v-if="error" class="mt-2 text-sm text-danger-500">{{ errorMessage }}</p>
  </div>
</template>

<script lang="ts" setup>
import type { BaseValidation } from "@vuelidate/core";
import { computed, onMounted, ref } from "vue";

interface Props {
  validationField?: BaseValidation<any, any>;
  hint?: string;
  label?: string;
  labelFor?: string;
  required?: boolean;
}

const props = withDefaults(defineProps<Props>(), {});

const idLabel = ref();
const slot_container = ref<HTMLElement>();

onMounted(() => {
  const inputs = slot_container.value.getElementsByTagName("input") ?? slot_container.value.getElementsByTagName("textarea") ?? slot_container.value.getElementsByTagName("select");

  if (inputs != null && inputs.length > 0) {
    idLabel.value = props.labelFor ?? inputs[0]?.id;
  }
});

const errorMessage = computed(() => {
  if (props.validationField == null) {
    return;
  }
  if (error) {
    return props.validationField?.$errors[0]?.$message;
  }
  return null;
});

const error = computed(() => {
  if (props.validationField == null) {
    return;
  }

  return props.validationField!.$invalid;
});
</script>

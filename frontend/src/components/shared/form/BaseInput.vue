<template>
  <input :id="id" v-model="data" :class="[errorClasses]" :placeholder="placeholder" class="shadow-sm mt-1 block w-full sm:text-sm border rounded-md" type="text">
</template>

<script lang="ts" setup>
import { useVModel } from "@vueuse/core";

interface Props {
  id?: string;
  modelValue?: any;
  placeholder?: string;
  error?: boolean;
}

const emit = defineEmits([ "update:modelValue" ]);

const props = withDefaults(defineProps<Props>(), {
  id: () => "baseinput-" + getCurrentInstance().uid.toString(),
  error: null,
});

const data = useVModel(props, "modelValue", emit);

const errorClasses = computed(() => {
  if (props.error === true) {
    return "border-danger-300 focus:ring-danger-800 focus:border-danger-800 bg-danger-50";
  } else if (props.error === false) {
    return "border-success-300 focus:ring-success-800 focus:border-success-800 bg-success-50";
  } else {
    return "focus:ring-primary-800 focus:border-primary-800 border-primary-300";
  }
});
</script>

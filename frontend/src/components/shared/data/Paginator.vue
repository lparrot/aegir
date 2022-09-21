<template>
  <div v-if="modelValue" class="flex items-center gap-8">
    <div class="flex items-center gap-2">
      <mdi-chevron-double-left :class="{'text-primary-400': isFirstPage}" class="h-6 w-6 cursor-pointer p-1 rounded-full hover:bg-primary-100" @click="updatePage(-10)"/>
      <mdi-chevron-left :class="{'text-primary-400': isFirstPage}" class="h-6 w-6 cursor-pointer p-1 rounded-full hover:bg-primary-100" @click="updatePage(-1)"/>
      <mdi-chevron-right :class="{'text-primary-400': isLastPage}" class="h-6 w-6 cursor-pointer p-1 rounded-full hover:bg-primary-100" @click="updatePage(1)"/>
      <mdi-chevron-double-right :class="{'text-primary-400': isLastPage}" class="h-6 w-6 cursor-pointer p-1 rounded-full hover:bg-primary-100" @click="updatePage(10)"/>
    </div>

    <div class="flex items-baseline gap-2">
      <input v-model="modelValue.page" class="p-0 text-right border-0 border-b border-b-primary w-20 text-sm" min="1" step="1" type="number">
      <div>/</div>
      <div>21</div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { withDefaults } from "vue";

interface Props {
  modelValue: AppPagination;
}

const props = withDefaults(defineProps<Props>(), {});

const emit = defineEmits<{
  (e: "update:modelValue"): any
  (e: "change"): any
}>();

const { modelValue } = useVModels(props, emit);

modelValue.value = resolveRef(Object.assign({}, { size: 15, page: 1, count: 0, totalPage: 0, order: { asc: true } }, props.modelValue)).value;

const isFirstPage = computed(() => {
  return modelValue.value.page === 1;
});

const isLastPage = computed(() => {
  return modelValue.value.page === modelValue.value.totalPage;
});

const updatePage = (count) => {
  if (modelValue.value.page + count < 1) {
    modelValue.value.page = 1;
  } else if ((modelValue.value.page + count) > modelValue.value.totalPage) {
    modelValue.value.page = modelValue.value.totalPage;
  } else {
    modelValue.value.page = modelValue.value.page + count;
    console.log(modelValue.value);
  }
};

</script>

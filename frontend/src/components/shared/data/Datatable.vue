<template>
  <div class="shadow border-b border-primary-200 sm:rounded-lg min-w-full overflow-auto">
    <table class="table-auto min-w-full divide-y divide-gray-200">
      <caption v-if="caption">{{ caption }}</caption>
      <thead class="bg-gray-200">
      <tr>
        <th v-for="field in fields" :key="`header-${field.key}`" class="py-3 px-3 text-left" scope="col">
          <slot :name="`header(${field.key})`">
            <span v-if="field.label">{{ field.label }}</span>
          </slot>
        </th>
      </tr>
      </thead>

      <tbody class="bg-white">
      <template v-if="items != null && items.length">
        <tr v-for="(item, itemIndex) in items" :key="`item-${get(item, idField)}`" :class="[{'hover:bg-primary-100 cursor-pointer': selectable, 'odd:bg-white even:bg-slate-50': striped}]" class="border-b border-b-primary-200" @click="onRowClick($event, item)">
          <td v-for="field in fields" :key="'field-' + field.key + '-'+itemIndex" class="py-2 px-3 whitespace-nowrap">
            <div :data-prevent-click="field.preventClick" class="item inline">
              <slot :field="field" :item="item" :name="`cell(${field.key})`" :value="getValue(field, item)">
                <span>{{ getValue(field, item) }}</span>
              </slot>
            </div>
          </td>
        </tr>
      </template>
      <tr v-else>
        <td class="flex justify-center">No content ...</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts" setup>
import get from "lodash/get";

interface Props {
  caption?: string
  fields?: DatatableField[]
  idField?: string
  items?: any[],
  selectable?: boolean
  striped?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  idField: "id",
});

const emit = defineEmits<{ (e: "row-click", item: any): void }>();

const onRowClick = (event, item) => {
  const element: HTMLElement = event.target.closest(".item");
  if (element?.dataset?.preventClick) {
    event.preventDefault();
  } else {
    emit("row-click", item);
  }
};

const getValue = (field, item) => field.transform != null ? field.transform(get(item, field.fieldName ?? field.key), item) : get(item, field.fieldName ?? field.key);
</script>

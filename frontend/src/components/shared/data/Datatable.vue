<template>
  <div class="shadow border-b border-primary-200 sm:rounded-lg min-w-full overflow-auto">
    <table class="table-auto min-w-full divide-y divide-gray-200">
      <caption v-if="caption">{{ caption }}</caption>
      <thead class="bg-gray-200">
      <tr>
        <th v-for="field in fields" :key="`header-${field.key}`" class="py-3 px-3 text-left">
          <span v-if="field.label">{{ field.label }}</span>
        </th>
      </tr>
      </thead>

      <tbody class="bg-white">
      <template v-if="items != null && items.length">
        <tr v-for="(item, itemIndex) in items" :key="`item-${get(item, idField)}`" :class="[{'hover:bg-primary-100 cursor-pointer': selectable}]" class="border-b border-b-primary-200">
          <td v-for="field in fields" :key="'field-' + field.key + '-'+itemIndex" class="py-2 px-3 whitespace-nowrap">
            <slot :field="field" :item="item" :name="`cell(${field.key})`" :value="getValue(field, item)">
              <span>{{ getValue(field, item) }}</span>
            </slot>
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
}

const props = withDefaults(defineProps<Props>(), {
  idField: "id",
});

const getValue = (field, item) => field.transform != null ? field.transform(get(item, field.fieldName ?? field.key)) : get(item, field.fieldName ?? field.key);
</script>

<template>
  <table>
    <caption v-if="caption">{{ caption }}</caption>
    <thead>
    <tr>
      <th v-for="field in fields" :key="`header-${field.key}`">
        <span v-if="field.label">{{ field.label }}</span>
      </th>
    </tr>
    </thead>

    <tbody>
    <template v-if="items != null && items.length">
      <tr v-for="(item, itemIndex) in items" :key="`item-${get(item, idField)}`">
        <td v-for="field in fields" :key="'field-' + field.key + '-'+itemIndex">
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
</template>

<script lang="ts" setup>
import get from "lodash/get";

interface Props {
  caption?: string
  fields?: DatatableField[]
  idField?: string
  items?: any[],
}

const props = withDefaults(defineProps<Props>(), {
  idField: "id",
});

const getValue = (field, item) => field.transform != null ? field.transform(get(item, field.fieldName ?? field.key)) : get(item, field.fieldName ?? field.key);
</script>

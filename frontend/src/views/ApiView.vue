<template>
  <div>
    <!--    <pre>{{ columns }}</pre>-->
    <table class="w-full">
      <thead>
      <tr class="text-left">
        <th scope="col">#</th>
        <th scope="col">Nom</th>
        <th scope="col">Type</th>
        <th class="text-center" scope="col">Taille</th>
        <th class="text-center" scope="col">Nullable ?</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="column in columns" :key="column.name" class="odd:bg-white even:bg-secondary-50 h-12">
        <td>
          <div class="flex gap-1 items-center mx-1">
            <mdi-key-variant v-if="column.primaryKey" class="text-orange-400"/>
            <mdi-key-variant v-if="column.foreignKey" class="text-green-800"/>
            <mdi-key-variant v-if="!column.primaryKey && column.uniqueKey" class="text-red-700"/>
          </div>
        </td>
        <td class="text-secondary-500">{{ column.name }}</td>
        <td>{{ column.type }}</td>
        <td class="text-center">
          <span>{{ column.size }}</span>
        </td>
        <td class="text-center">
          <mdi-check v-if="column.nullable" class="text-green-800 inline"/>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script lang="ts" setup>
import { TableColumnDto } from "back_types";
import { Ref } from "vue";

const route = useRoute();
const columns: Ref<TableColumnDto[]> = ref<TableColumnDto[]>([]);

watch(
  () => route.params?.table,
  async (value) => {
    const val: string = value as string;
    if (value != null && val.trim()?.length > 0) {
      const { success, result } = await api.getColumns(val);
      if (success) {
        columns.value = result;
      }
    }
  },
  { immediate: true },
);
</script>

<template>
  <section>
    {{ status.name }}

    <q-table :columns="columns" :rows="tasks" dense flat>
      <template #body-cell-createdAt="props">
        <q-td :props="props">
          <div>{{ $dayjs(props.value).format("DD/MM/YYYY HH:mm") }}</div>
        </q-td>
      </template>
    </q-table>
  </section>
</template>

<script lang="ts" setup>
import { TaskInfo, TaskStatusInfo } from "app/.generated/rest";
import { QTableColumn } from "quasar";

defineProps<{
  status: TaskStatusInfo,
  tasks: TaskInfo
}>();

const columns: QTableColumn[] = [
  { name: "name", field: "name", label: "Tâche", align: "left" },
  { name: "createdAt", field: "createdAt", label: "Crée le", align: "left" },
];
</script>

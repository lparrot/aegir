<template>
  <section>
    <q-expansion-item class="q-mt-md" default-opened dense switch-toggle-side>
      <template v-slot:header>
        <q-badge :color="status.color">{{ status.name }}</q-badge>
      </template>

      <q-table :columns="columns" :rows="tasks" bordered class="q-mt-md" dense flat>
        <template #body-cell-assigned="props">
          <q-td :props="props">
            <q-avatar v-if="props.value != null" color="primary" size="sm" text-color="white">{{ getInitials(props.value?.username) }}</q-avatar>
            <q-avatar v-else color="green" size="sm" text-color="white">?</q-avatar>
          </q-td>
        </template>

        <template #body-cell-createdAt="props">
          <q-td :props="props">
            <div>{{ $dayjs(props.value).format("DD/MM/YYYY HH:mm") }}</div>
          </q-td>
        </template>
      </q-table>
    </q-expansion-item>
  </section>
</template>

<script lang="ts" setup>
import { TaskInfo, TaskStatusInfo } from "app/.generated/rest";
import { QTableColumn } from "quasar";
import { getInitials } from "src/utils/string.utils";
import { Ref, ref } from "vue";

defineProps<{
  status: TaskStatusInfo,
  tasks: TaskInfo
}>();

const columns: Ref<QTableColumn[]> = ref<QTableColumn[]>();

columns.value = [
  { name: "name", field: "name", label: "Tâche", align: "left" },
  { name: "assigned", field: "assigned", label: "Assigné à", align: "center", style: "width: 80px" },
  { name: "createdAt", field: "createdAt", label: "Crée le", align: "left", style: "width: 120px" },
];
</script>

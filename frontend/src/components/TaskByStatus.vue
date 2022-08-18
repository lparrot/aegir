<template>
  <section>
    <q-expansion-item class="q-mt-md" default-opened dense switch-toggle-side>
      <template v-slot:header>
        <q-badge :color="status.color">{{ status.name }}</q-badge>
      </template>

      <q-table :columns="columns" :grid="$q.screen.lt.md" :rows="tasks" bordered class="q-mt-md" dense flat hide-header>
        <template #item="props">
          <div class="row content-stretch q-pa-xs col-xs-12 col-sm-6 col-md-4 ">
            <q-card class="full-width">
              <q-card-section class="flex items-center">
                <q-avatar v-if="props.row.assigned != null" color="primary" size="sm" text-color="white">{{ getInitials(props.row.assigned?.username) }}</q-avatar>
                <q-avatar v-else color="green" size="sm" text-color="white">?</q-avatar>
                <q-space></q-space>
                <div class="text-caption">{{ $dayjs(props.value).format("DD/MM/YYYY HH:mm") }}</div>
              </q-card-section>

              <q-card-section>
                <div class="cursor-pointer" @click="onTaskClick(props)">{{ props.row.name }}</div>
              </q-card-section>
            </q-card>
          </div>
        </template>

        <template #body-cell-name="props">
          <q-td :props="props">
            <div class="cursor-pointer" @click="onTaskClick(props)">{{ props.value }}</div>
          </q-td>
        </template>

        <template #body-cell-assigned="props">
          <q-td :props="props">
            <q-avatar v-if="props.value != null" color="primary" size="sm" text-color="white">{{ getInitials(props.value.username) }}</q-avatar>
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
import { getInitials } from "src/utils/string.utils";
import { TaskInfo, TaskStatusInfo } from "app/.generated/rest";
import { Dialog, QTableColumn } from "quasar";
import { Ref, ref } from "vue";
import TaskDetail from "components/dialogs/DialogTaskDetail.vue";

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

const onTaskClick = (props) => {
  Dialog.create({
    component: TaskDetail,
    persistent: true,
    maximized: true,
    componentProps: {
      taskId: props.row.id,
    },
  });
};
</script>

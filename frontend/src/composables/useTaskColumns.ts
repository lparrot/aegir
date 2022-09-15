import TaskColumnContent from "@/components/tasks/TaskColumnContent.vue";
import TaskColumnMember from "@/components/tasks/TaskColumnMember.vue";
import TaskColumnStatus from "@/components/tasks/TaskColumnStatus.vue";
import { Ref } from "vue";

const defaultColumns: AppTaskColumn[] = [
  { id: "content", header: "Contenu", headerClass: "text-left", rowClass: "p-2 whitespace-nowrap", component: markRaw(TaskColumnContent) },
  { id: "assigned", header: "Assigné à", rowClass: "p-2 justify-center", componentWidth: "100px", component: markRaw(TaskColumnMember) },
  { id: "status", header: "Statut", rowClass: "justify-center", componentWidth: "150px", component: markRaw(TaskColumnStatus) },
];

const columns: Ref<AppTaskColumn[]> = ref(defaultColumns);

export function useTaskColumns() {
  return {
    columns,
  };
}

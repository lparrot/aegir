import TaskColumnContent from "@/components/tasks/TaskColumnContent.vue";
import TaskColumnMember from "@/components/tasks/TaskColumnMember.vue";
import TaskColumnStatus from "@/components/tasks/TaskColumnStatus.vue";
import { Ref } from "vue";

const defaultColumns: AppTaskColumn[] = [
  { id: "content", header: "Contenu", rowClass: "p-2 whitespace-nowrap", component: TaskColumnContent },
  { id: "assigned", header: "Assigné à", rowClass: "p-2 justify-center", componentWidth: "100px", component: TaskColumnMember },
  { id: "status", header: "Statut", rowClass: "justify-center", componentWidth: "150px", component: TaskColumnStatus },
];

const columns: Ref<AppTaskColumn[]> = ref(defaultColumns);

export function useTaskColumns() {
  return {
    columns,
  };
}

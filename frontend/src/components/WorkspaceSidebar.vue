<template>
  <SidebarContent>
    <div class="flex justify-between">
      <div>Espace de travail</div>
      <div>
        <mdi-dots-horizontal class="h-5 w-5 cursor-pointer"/>
      </div>
    </div>

    <Select v-model="storageSidebar.workspace_selected" :placeholder="selectedWorkspaceName" class="mt-4" null-label="Choisissez...">
      <SelectItem v-for="workspace in workspaces" :key="workspace.id" :label="workspace.name" :value="workspace.id"/>
    </Select>

    <template v-if="selectedWorkspace != null">
      <slot></slot>
    </template>
  </SidebarContent>
</template>

<script lang="ts" setup>
import useAppLocalStorage from "@use/useAppLocalStorage";
import useWorkspaceSidebar from "@use/useWorkspaceSidebar";

const { storageSidebar } = useAppLocalStorage();

const { workspaces, selectedWorkspaceName, selectedWorkspace, fetchWorkspaces } = useWorkspaceSidebar({ with_watch: true });

await fetchWorkspaces();
</script>

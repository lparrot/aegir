<template>
  <q-layout view="hHh Lpr lFf">
    <q-header reveal>
      <q-toolbar class="q-pr-none">
        <q-btn aria-label="Menu" dense flat icon="menu" round @click="toggleLeftDrawer"/>

        <q-toolbar-title class="flex items-baseline" shrink>
          <q-btn :to="{name: 'index'}" flat unelevated>
            <div>{{ appStore?.informations?.app?.title?.toUpperCase() }}</div>
            <div class="q-ml-sm text-subtitle2 text-grey-4">v{{ appStore?.informations?.app?.version }}</div>
          </q-btn>
        </q-toolbar-title>

        <q-space/>

        <template v-if="authStore.isLoggedIn">
          <q-btn-dropdown dense flat padding="sm" size="sm" stretch title="Avatar">
            <template #label>
              <div class="text-subtitle2">{{ authStore.user.username }}</div>
            </template>
            <q-list>
              <q-item v-close-popup :to="{name: 'profile'}" clickable>
                <q-item-section avatar>
                  <q-icon name="face"/>
                </q-item-section>
                <q-item-section>Profil</q-item-section>
              </q-item>
              <q-item v-close-popup clickable @click="onDisconnect">
                <q-item-section avatar>
                  <q-icon name="logout"/>
                </q-item-section>
                <q-item-section>Deconnexion</q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
        </template>

        <template v-else>
          <q-btn :to="{name: 'login'}" flat icon="face" label="Connexion" title="Login"/>
        </template>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawer" :width="300" bordered class="bg-grey-2 scroll-light" show-if-above>
      <application-menu></application-menu>

      <template v-if="authStore.isLoggedIn">
        <q-separator class="q-mt-sm"/>

        <q-select v-model="storageSidebar.project_selected" :options="projectStore.userProjects" emit-value filled label="Projet en cours" map-options option-label="name" option-value="id" square @update:model-value="onProjectSelected"></q-select>

        <q-separator/>

        <template v-if="projectStore.selectedProject != null">
          <q-expansion-item :default-opened="storageSidebar.module_selected === 'bookmarks'" group="modules" header-class="text-primary" label="FAVORIS" @after-show="onOpenModule('bookmarks')">
            <q-card>
              <q-card-section>

              </q-card-section>
            </q-card>
          </q-expansion-item>

          <q-separator/>

          <q-expansion-item :default-opened="storageSidebar.module_selected === 'workspaces'" group="modules" header-class="text-primary" label="ESPACES" @after-show="onOpenModule('workspaces')">
            <q-card>
              <q-card-section>
                <q-btn class="full-width q-mb-sm text-grey-8 text-weight-bolder" color="grey-4" dense icon="add" label="Nouvel espace" size="sm" unelevated @click="showDialogCreateWorkspace"></q-btn>

                <tree-project-item @item-selected="closeDrawerIfSizeLtMedium()"></tree-project-item>
              </q-card-section>
            </q-card>
          </q-expansion-item>

          <q-separator/>

          <q-expansion-item :default-opened="storageSidebar.module_selected === 'dashboards'" group="modules" header-class="text-primary" label="TABLEAUX DE BORD" @after-show="onOpenModule('dashboards')">
            <q-card>
              <q-card-section>

              </q-card-section>
            </q-card>
          </q-expansion-item>

          <q-separator/>

          <q-expansion-item :default-opened="storageSidebar.module_selected === 'documents'" group="modules" header-class="text-primary" label="DOCUMENTS" @after-show="onOpenModule('documents')">
            <q-card>
              <q-card-section>

              </q-card-section>
            </q-card>
          </q-expansion-item>

          <q-separator/>

        </template>

        <template v-if="$security.isInRoles(['ADMIN'])">
          <q-expansion-item :default-opened="storageSidebar.module_selected === 'admin'" group="modules" header-class="text-primary" label="ADMIN" @after-show="onOpenModule('admin')">
            <q-card>
              <q-card-section>
                <q-list dense>
                  <q-item :to="{name: 'admin-connections'}" class="q-btn--rounded" clickable>
                    <q-item-section side>
                      <q-icon name="lan"></q-icon>
                    </q-item-section>
                    <q-item-section>Sessions actives</q-item-section>
                  </q-item>
                  <q-item :to="{name: 'admin-swagger'}" class="q-btn--rounded" clickable>
                    <q-item-section side>
                      <q-icon name="api"></q-icon>
                    </q-item-section>
                    <q-item-section>API Swagger</q-item-section>
                  </q-item>
                </q-list>
              </q-card-section>
            </q-card>
          </q-expansion-item>

          <q-separator/>
        </template>
      </template>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { Dialog, Notify, QTree, useQuasar } from "quasar";
import { useAuthStore } from "stores/auth";
import { useAppStore } from "stores/app";
import { useRouter } from "vue-router";
import ApplicationMenu from "components/ApplicationMenu.vue";
import { useProjectStore } from "stores/project";
import CreateWorkspace, { FormCreateWorkspace } from "components/dialogs/DialogCreateWorkspace.vue";
import useAppLocalStorage from "src/composables/useAppLocalStorage";
import TreeProjectItem from "components/TreeProjectItem.vue";
import { api } from "boot/axios";
import { EnumProjectItemType } from "app/.generated/rest";

////////////////
// Composables
////////////////
const router = useRouter();
const $q = useQuasar();
const projectStore = useProjectStore();
const authStore = useAuthStore();
const appStore = useAppStore();
const { storageSidebar } = useAppLocalStorage();

////////////////
// References
////////////////
const leftDrawer = ref();
const itemsTree = ref<QTree>(null);

const closeDrawerIfSizeLtMedium = () => {
  if ($q.screen.lt.md) {
    leftDrawer.value = false;
  }
};

const toggleLeftDrawer = () => {
  leftDrawer.value = !leftDrawer.value;
};

const onDisconnect = () => {
  Dialog.create({
    title: "Confirmation",
    message: "Etes vous sûr de vouloir vous deconnecter ?",
    cancel: true,
    persistent: true,
  })
    .onOk(async () => {
      await authStore.disconnect();
      Notify.create({
        message: `Vous êtes maintenant deconnecté`,
        color: "positive",
      });
    });
};

const onProjectSelected = () => {
  storageSidebar.value.item_selected = null;
  storageSidebar.value.items_expanded = [];
};

const onOpenModule = (id) => {
  storageSidebar.value.module_selected = id;
};

const showDialogCreateWorkspace = () => {
  closeDrawerIfSizeLtMedium();

  $q.dialog({
    component: CreateWorkspace,
  })
    .onOk(async (formData: FormCreateWorkspace) => {
      const { success, result } = await api.createProjectItem({
        name: formData.name,
        projectId: projectStore.selectedProject.id,
        type: EnumProjectItemType.WORKSPACE,
        statuses: [],
      });

      if (success) {
        storageSidebar.value.item_selected = result.id;
        await router.push({ name: "tasks" });
        await projectStore.fetchSelectedProject();
      }
    });
};
</script>

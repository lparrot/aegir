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
              <q-item v-close-popup clickable>
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

    <q-drawer v-model="leftDrawer" bordered class="bg-grey-2" show-if-above>
      <q-scroll-area class="fit" visible>
        <application-menu></application-menu>

        <q-separator class="q-mt-lg"/>

        <q-expansion-item group="modules" header-class="text-primary" label="FAVORIS">
          <q-card>
            <q-card-section>

            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-separator/>

        <q-expansion-item group="modules" header-class="text-primary" label="ESPACES">
          <q-card>
            <q-card-section>
              <q-tree v-model:selected="projectStore.selectedItemId" :nodes="items" accordion dense label-key="name" no-connectors node-key="id">
                <template v-slot:default-header="prop">
                  <div :class="{'bg-grey-2': prop.key === projectStore.selectedItemId}" class="row items-center full-width q-py-xs q-px-xs rounded-borders">
                    <q-icon class="q-mr-sm" size="16px" v-bind="getProjectItemIconProps(prop.node?.type)"/>
                    <div class="text-primary">{{ prop.node.name }}</div>
                  </div>
                </template>
              </q-tree>
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-separator/>

        <q-expansion-item group="modules" header-class="text-primary" label="TABLEAUX DE BORD">
          <q-card>
            <q-card-section>

            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-separator/>

        <q-expansion-item group="modules" header-class="text-primary" label="DOCUMENTS">
          <q-card>
            <q-card-section>

            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-separator/>

      </q-scroll-area>
    </q-drawer>

    <q-page-container>
      <router-view/>
    </q-page-container>
  </q-layout>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";
import { Dialog, Notify } from "quasar";
import { useAuthStore } from "stores/auth";
import { useAppStore } from "stores/app";
import { useRouter } from "vue-router";
import { useMenu } from "src/composables/useMenu";
import ApplicationMenu from "components/ApplicationMenu.vue";
import { api } from "boot/axios";
import { useProjectStore } from "stores/project";

const authStore = useAuthStore();
const appStore = useAppStore();
const projectStore = useProjectStore();
const router = useRouter();

const leftDrawer = ref();
const items = ref();

items.value = await api.$get(`/api/projects/${ 1 }/items`);

const { refreshMenu, setMenuDefault } = useMenu();

setMenuDefault([
  { type: "header", label: "Menu" },
  { icon: "home", label: "Accueil", to: { name: "index" } },
  { icon: "view_module", label: "Dashboard", to: { name: "dashboard" } },
]);

const getProjectItemIconProps = (type) => {
  switch (type) {
    case "WORKSPACE":
      return { name: "workspaces", color: "blue" };
    case "FOLDER":
      return { name: "folder", color: "orange" };
    case "VIEW":
      return { name: "view_sidebar", color: "green" };
    default:
      return {};
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

watch(
  () => authStore.isLoggedIn,
  () => {refreshMenu();},
  { deep: true });

watch(
  () => projectStore.selectedItemId,
  () => {projectStore.fetchSelectedItem();},
  { immediate: true },
);

</script>

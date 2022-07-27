<template>
  <q-layout view="hHh Lpr lFf">
    <q-header reveal>
      <q-toolbar>
        <q-btn aria-label="Menu" dense flat icon="menu" round @click="toggleLeftDrawer"/>

        <q-toolbar-title class="flex items-baseline">
          <q-btn :to="{name: 'index'}" flat unelevated>
            <div>{{ appStore?.informations?.app?.title?.toUpperCase() }}</div>
            <div class="q-ml-sm text-subtitle2 text-grey-4">v{{ appStore?.informations?.app?.version }}</div>
          </q-btn>
        </q-toolbar-title>

        <div>
          <template v-if="authStore.isLoggedIn">
            <q-btn dense flat size="sm" title="Avatar">
              <div class="text-subtitle2">{{ authStore.user.username }}</div>
              <q-menu fit transition-hide="jump-up" transition-show="jump-down">
                <q-list bordered>
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
              </q-menu>
            </q-btn>
          </template>

          <template v-else>
            <q-btn :to="{name: 'login'}" dense flat icon="face" round title="Login"/>
          </template>
        </div>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawer" bordered class="bg-grey-2" show-if-above>
      <q-scroll-area class="fit" visible>
        <q-list>

          <template v-for="(menuItem, menuItemIndex) in menu" :key="'item' + menuItemIndex">
            <q-separator v-if="menuItem.type === 'separator'" class="q-my-sm" inset/>

            <q-item-label v-else-if="menuItem.type === 'header'" header>{{ menuItem.label }}</q-item-label>

            <template v-else-if="menuItem.children != null">
              <q-expansion-item :default-opened="menuItem.defaultOpened ?? true" :icon="menuItem.icon" :label="menuItem.label" dense>
                <q-item v-for="(subMenuItem, subMenuItemIndex) in menuItem.children" :key="`subitem${menuItemIndex}-${subMenuItemIndex}`" v-ripple :to="subMenuItem.to ?? null" clickable dense inset-level="0.5" @click="subMenuItem.action ? subMenuItem.action() : null">
                  <q-item-section avatar>
                    <q-icon :name="subMenuItem.icon"/>
                  </q-item-section>
                  <q-item-section>
                    {{ subMenuItem.label }}
                  </q-item-section>
                </q-item>
              </q-expansion-item>
            </template>

            <template v-else>
              <q-item v-ripple :to="menuItem.to ?? null" clickable dense @click="menuItem.action ? menuItem.action() : null">
                <q-item-section avatar>
                  <q-icon :name="menuItem.icon"/>
                </q-item-section>
                <q-item-section>
                  {{ menuItem.label }}
                </q-item-section>
              </q-item>
            </template>
          </template>
        </q-list>
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

const authStore = useAuthStore();
const appStore = useAppStore();
const router = useRouter();

const leftDrawer = ref();

const { menu, refreshMenu } = useMenu([
  { type: "header", label: "Menu de l'application", showIf: () => authStore.isLoggedIn },
  {
    icon: "space_dashboard", label: "Moules", children: [
      { icon: "view_module", label: "Dashboard", to: "/dashboard" },
    ],
  },
]);

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
  (value) => {
    refreshMenu();
  },
  { deep: true, immediate: true });
</script>

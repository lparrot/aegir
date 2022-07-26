<template>
  <q-layout view="hHh Lpr lFf">
    <q-header reveal>
      <q-toolbar>
        <q-btn aria-label="Menu" dense flat icon="menu" round @click="toggleLeftDrawer"/>

        <q-toolbar-title class="flex items-baseline">
          <q-btn flat to="/" unelevated>
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
            <q-btn dense flat icon="face" round title="Login" to="/login"/>
          </template>
        </div>
      </q-toolbar>
    </q-header>

    <q-drawer v-model="leftDrawer" bordered class="bg-grey-2" show-if-above>
      <q-scroll-area class="fit" visible>
        <q-list>

          <template v-for="(menuItem, menuItemIndex) in menuList" :key="'item' + menuItemIndex">
            <template v-if="showItem(menuItem)">
              <template v-if="menuItem.type === 'header'">
                <q-separator v-if="menuItem.separator" inset/>

                <q-item-label header>{{ menuItem.label }}</q-item-label>

                <template v-if="menuItem.children">
                  <template v-for="(subMenuItem, subMenuItemIndex) in menuItem.children" :key="`subitem${menuItemIndex}-${subMenuItemIndex}`">
                    <q-item v-if="showItem(subMenuItem)" v-ripple :to="subMenuItem.to ?? null" clickable @click="subMenuItem.action ? subMenuItem.action() : null">
                      <q-item-section avatar>
                        <q-icon :name="subMenuItem.icon"/>
                      </q-item-section>
                      <q-item-section>
                        {{ subMenuItem.label }}
                      </q-item-section>
                    </q-item>
                  </template>
                </template>
              </template>

              <template v-else>
                <q-separator v-if="menuItem.separator" inset/>

                <q-item v-ripple :to="menuItem.to ?? null" clickable @click="menuItem.action ? menuItem.action() : null">
                  <q-item-section avatar>
                    <q-icon :name="menuItem.icon"/>
                  </q-item-section>
                  <q-item-section>
                    {{ menuItem.label }}
                  </q-item-section>
                </q-item>
              </template>

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
import { ref } from "vue";
import { Notify, useQuasar } from "quasar";
import { useAuthStore } from "stores/auth";
import { useAppStore } from "stores/app";

const $q = useQuasar();
const authStore = useAuthStore();
const appStore = useAppStore();

const leftDrawer = ref();

const showItem = (item) => {
  return item.showIf == null ? true : typeof item.showIf === "function" ? item.showIf() : item.showIf;
};

const menuList = [
  {
    label: "Menu", type: "header", children: [
      { icon: "dashboard", label: "Dashboard", to: "/dashboard", showIf: () => authStore.isLoggedIn },
      { icon: "face", label: "Login", to: "/login", showIf: () => !authStore.isLoggedIn },
    ],
  },
  {
    label: "API", type: "header", showIf: () => authStore.isLoggedIn, separator: true, children: [
      { icon: "table_chart", label: "Contenu", to: "/admin/contents" },
    ],
  },
  {
    label: "Administration", type: "header", showIf: () => authStore.isLoggedIn, separator: true, children: [
      { icon: "dns", label: "Serveur", to: "/admin/server" },
      { icon: "people", label: "Utilisateurs", to: "/admin/users" },
    ],
  },
];

const toggleLeftDrawer = () => {
  leftDrawer.value = !leftDrawer.value;
};

const onDisconnect = () => {
  $q.dialog({
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
</script>

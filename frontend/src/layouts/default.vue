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
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quidem, eius reprehenderit eos corrupti
              commodi magni quaerat ex numquam, dolorum officiis modi facere maiores architecto suscipit iste
              eveniet doloribus ullam aliquid.
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-separator/>

        <q-expansion-item group="modules" header-class="text-primary" label="ESPACES">
          <q-card>
            <q-card-section>
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quidem, eius reprehenderit eos corrupti
              commodi magni quaerat ex numquam, dolorum officiis modi facere maiores architecto suscipit iste
              eveniet doloribus ullam aliquid.
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-separator/>

        <q-expansion-item group="modules" header-class="text-primary" label="TABLEAUX DE BORD">
          <q-card>
            <q-card-section>
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quidem, eius reprehenderit eos corrupti
              commodi magni quaerat ex numquam, dolorum officiis modi facere maiores architecto suscipit iste
              eveniet doloribus ullam aliquid.
            </q-card-section>
          </q-card>
        </q-expansion-item>

        <q-separator/>

        <q-expansion-item group="modules" header-class="text-primary" label="DOCUMENTS">
          <q-card>
            <q-card-section>
              Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quidem, eius reprehenderit eos corrupti
              commodi magni quaerat ex numquam, dolorum officiis modi facere maiores architecto suscipit iste
              eveniet doloribus ullam aliquid.
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

const authStore = useAuthStore();
const appStore = useAppStore();
const router = useRouter();

const leftDrawer = ref();

const { refreshMenu, setMenuDefault } = useMenu();

setMenuDefault([
  { type: "header", label: "Menu" },
  { icon: "home", label: "Accueil", to: { name: "index" } },
  { icon: "view_module", label: "Dashboard", to: { name: "dashboard" } },
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
  () => {refreshMenu();},
  { deep: true });
</script>

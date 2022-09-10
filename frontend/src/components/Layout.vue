<template>
  <section class="flex w-full">
    <aside class="flex flex-nowrap flex-col justify-between items-center bg-primary-700 min-w-[60px] py-2 text-primary-200">
      <div class="flex flex-col items-center gap-6 mx-4">
        <RouterLink :to="{name: 'home'}">
          <img alt="logo" class="h-8 w-8 mb-2" src="/logo.png">
        </RouterLink>
        <RouterLink :to="{name: ''}">
          <mdi-view-grid-outline class="h-10 w-10 p-2 -m-2 rounded cursor-pointer hover:bg-primary-500"/>
        </RouterLink>
        <RouterLink :to="{name: ''}">
          <mdi-bell-outline class="h-10 w-10 p-2 -m-2 rounded cursor-pointer hover:bg-primary-500"/>
        </RouterLink>
        <RouterLink :to="{name: ''}">
          <mdi-inbox class="h-10 w-10 p-2 -m-2 rounded cursor-pointer hover:bg-primary-500"/>
        </RouterLink>
        <RouterLink :to="{name: ''}">
          <mdi-calendar-month class="h-10 w-10 p-2 -m-2 rounded cursor-pointer hover:bg-primary-500"/>
        </RouterLink>
      </div>

      <div class="flex flex-col items-center gap-6">
        <div class="font-bold">{{ breakpoint }}</div>

        <Popover #default="{open}" class="relative inline-block">
          <Float :offset="8" enter="transition ease-out duration-100" enter-from="transform opacity-0 scale-95" enter-to="transform opacity-100 scale-100" flip leave="transition ease-in duration-75" leave-from="transform opacity-100 scale-100" leave-to="transform opacity-0 scale-95" placement="right-end" portal>
            <PopoverButton>
              <div class="flex items-center gap-1 cursor-pointer rounded-full hover:bg-gray-600 duration-150">
                <div class="flex items-center justify-center text-lg rounded-full h-10 w-10 font-bold border-secondary-300 bg-secondary-100 text-secondary-300">
                  {{ getInitials(authStore.user?.sub) }}
                </div>
              </div>
            </PopoverButton>

            <PopoverPanel #default="{close}">
              <div class="rounded bg-white text-primary-500 border border-primary-200 shadow-3xl">
                <div class="flex items-baseline gap-2 p-4 border-b border-primary-200">
                  <img alt="logo" class="h-6 w-6" src="/logo.png">
                  <span class="text-lg">Connecté sous {{ authStore.user?.sub }}</span>
                </div>

                <div class="grid grid-cols-1 md:!grid-cols-2 lg:!grid-cols-3 gap-6 p-4 max-h-[500px] overflow-y-auto scrollbar scrollbar-thin scrollbar-thumb-gray-600 scrollbar-track-gray-300">
                  <div class="flex flex-col gap-3">
                    <div class="px-3 py-2 ml-6 text-primary-400">
                      Compte
                    </div>
                    <RouterLink :to="{name: 'profile'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-account/>
                      <span>Mon profil</span>
                    </RouterLink>
                    <div class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100 cursor-pointer text-danger-400" @click="logout(); close()">
                      <mdi-sign-out/>
                      <span>Déconnexion</span>
                    </div>
                  </div>

                  <div class="flex flex-col gap-3">
                    <div class="px-3 py-2 ml-6 text-primary-400">
                      Administration
                    </div>
                    <RouterLink :to="{name: 'admin-connected-users'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-account-network-outline/>
                      <span>Utilisateurs connectés</span>
                    </RouterLink>
                    <RouterLink :to="{name: 'admin-swagger'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-api/>
                      <span>API Swagger</span>
                    </RouterLink>
                    <RouterLink :to="{name: ''}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-plus/>
                      <span>Inviter des membres</span>
                    </RouterLink>
                  </div>

                  <div class="flex flex-col gap-3">
                    <div class="px-3 py-2 ml-6 text-primary-400">
                      Developpement
                    </div>
                    <RouterLink :to="{name: 'dev-accordion'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-view-sequential-outline/>
                      <span>Accordion</span>
                    </RouterLink>
                    <RouterLink :to="{name: 'dev-datatable'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-view-list/>
                      <span>Datatable</span>
                    </RouterLink>
                    <RouterLink :to="{name: 'dev-loader'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-timer-sand/>
                      <span>Loader</span>
                    </RouterLink>
                    <RouterLink :to="{name: 'dev-modal'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-animation-outline/>
                      <span>Modal</span>
                    </RouterLink>
                    <RouterLink :to="{name: 'dev-toast'}" class="flex items-center gap-2 px-3 py-2 rounded hover:bg-primary-100" @click="close">
                      <mdi-format-quote-close-outline/>
                      <span>Toast</span>
                    </RouterLink>
                  </div>
                </div>
              </div>
            </PopoverPanel>
          </Float>
        </Popover>
      </div>
    </aside>

    <aside ref="drawer_sidebar" :class="{'max-w-0': isSidebarClosed, 'max-w-full': !isSidebarClosed, 'fixed': isMobile}" class="relative flex flex-col flex-shrink-0 bg-primary-200 w-72 text-white transition-all duration-150 ease-in h-screen rounded-r-2xl border border-primary-300 shadow-2xl">

      <div class="absolute top-4 -right-4 rounded-full bg-white h-7 w-7 flex items-center justify-center border border-primary-300 hover:bg-primary-100 cursor-pointer" @click="toggle">
        <mdi-chevron-right v-if="isSidebarClosed" class="h-5 w-5 text-primary"/>
        <mdi-chevron-left v-else class="h-5 w-5 text-primary"/>
      </div>
    </aside>

    <div class="flex flex-col text-white h-screen w-full scrollbar scrollbar-thin scrollbar-thumb-gray-600 scrollbar-track-gray-300">
      <header v-if="data.title != null" class="flex px-8 py-4 text-3xl tracking-tight font-bold text-gray-900">
        <h1>{{ data.title }}</h1>
      </header>

      <main class="flex flex-col px-8 py-4 h-full w-full text-gray-900">
        <RouterView/>
      </main>
    </div>
  </section>

  <ToastGroup/>
  <Commander/>
  <Loader/>
</template>

<script lang="ts" setup>
import Commander from "@/components/Commander.vue";
import Loader from "@/components/shared/overlay/Loader.vue";
import ToastGroup from "@/components/shared/overlay/ToastGroup.vue";
import { useAppStore } from "@/stores/app";
import { useAuthStore } from "@/stores/auth";
import { Popover, PopoverButton, PopoverPanel } from "@headlessui/vue";
import useAegir from "@use/useAegir";
import useAppLocalStorage from "@use/useAppLocalStorage";
import useMenu from "@use/useMenu";
import useWebsocket, { getHeaders } from "@use/useWebsocket";
import { breakpointsTailwind, useBreakpoints, useTitle } from "@vueuse/core";
import { computed, reactive, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";

interface Data {
  opened: boolean;
  title: string;
}

/* COMPOSABLES */
const appStore = useAppStore();
const authStore = useAuthStore();
const { storageToken } = useAppLocalStorage();
const { dialog, toast } = useAegir();
const route = useRoute();
const router = useRouter();
const breakpoints = useBreakpoints(breakpointsTailwind);
const { menu, refreshMenu } = useMenu();
const socket = useWebsocket();

/* DATA */
const data = reactive<Data>({
  opened: false,
  title: null,
});

const drawer_sidebar = ref(null);
const isMobile = breakpoints.smaller("lg");
const sm = breakpoints.smaller("sm");
const md = breakpoints.between("sm", "md");
const lg = breakpoints.between("md", "lg");
const xl = breakpoints.between("lg", "xl");
const xxl = breakpoints.between("xl", "2xl");
const xxxl = breakpoints["2xl"];


/* INIT */
// onClickOutside(drawer_sidebar, (_event) => data.opened = false);
await appStore.getInformations();

/* COMPUTED */
const isSidebarClosed = computed(() => {
  return !data.opened;
});

const breakpoint = computed(() => {
  if (sm.value) {
    return "sm";
  }
  if (md.value) {
    return "md";
  }
  if (lg.value) {
    return "lg";
  }
  if (xl.value) {
    return "xl";
  }
  if (xxl.value) {
    return "2xl";
  }
  if (xxxl.value) {
    return "3xl";
  }
  return null;
});

/* HOOKS */
watch(route,
  (_route) => {
    // Modification du titre dans l'onglet et récupération de la partie du titre pour affichage en titre de page
    useTitle(_route.meta.title, { titleTemplate: "%s | " + appStore.informations.app.title });
    data.title = _route.meta.title;
  },
  { immediate: true },
);

watch(
  storageToken,
  async (_token) => {
    if (_token != null) {
      await authStore.getUser();
      socket.client.value.connectHeaders = getHeaders();
    }
    refreshMenu();
  },
);

watch(isMobile,
  (_isMobile) => {
    if (_isMobile) {
      data.opened = false;
    }
  },
);

/* METHODS */
const toggle = () => {
  data.opened = !data.opened;
};

const logout = async () => {
  dialog.create({
    title: "Deconnexion",
    message: "Etes vous sûr de vouloir vous déconnecter ?",
    async onOk() {
      await authStore.logout();
      await router.push({ name: "login" });
      toast.createToast({
        message: "Vous êtes maintenant déconnecté",
        title: "Deconnexion",
        type: "success",
      });
    },
  });
};
</script>

<template>
  <section class="flex w-full font-inter">

    <div v-if="isMobile && !isSidebarClosed" aria-hidden="true" class="fixed inset-0 bg-black/30"/>

    <aside ref="drawer_sidebar" :class="{'-translate-x-full': isSidebarClosed, 'translate-x-0': !isSidebarClosed, 'fixed': isMobile}" class="flex flex-col flex-shrink-0 bg-primary-800 w-72 text-white transition-all duration-150 ease-in h-screen">
      <div class="flex justify-between items-center h-12 py-4 px-4">
        <RouterLink is="div" :to="{name: 'home'}" class="cursor-pointer hover:bg-gray-600 px-2 -mx-2 py-1 -my-1 rounded flex gap-3 items-baseline">
          <div class="text-xl">{{ appStore.informations.app.title }}</div>
          <div>v{{ appStore.informations.app.version }}</div>
        </RouterLink>
        <div class="p-1 rounded-full hover:bg-primary-600" @click="data.opened = false">
          <XIcon v-show="!isSidebarClosed && isMobile" class="h-5 w-5 cursor-pointer"/>
        </div>
      </div>

      <!-- menu -->
      <div class="scrollbar scrollbar-thin scrollbar-thumb-gray-600 scrollbar-track-gray-300">
        <template v-for="menu in data.menuItems">
          <div v-if="menu.type === 'heading'" class="font-thin py-2 text-sm text-gray-300 px-4">
            {{ menu.label }}
          </div>

          <RouterLink v-else #default="{navigate, href, isExactActive}" :to="menu.route" custom>
            <a :class="{'bg-gray-500': isExactActive}" :href="href" class="block cursor-pointer my-1 py-1 px-6 hover:bg-gray-600 duration-150" @click="navigate">{{ menu.label }}</a>
          </RouterLink>
        </template>

        <div class="border-separate"></div>

        <!--
        <aeg-accordion contentClass="bg-gray-600" titleClass="text-lg">
          <aeg-accordion-item name="bookmarks" title="Favoris">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Aliquam aliquid aspernatur at, blanditiis
            perspiciatis reprehenderit tempore veniam vero voluptatibus? A ad atque debitis eaque illo ipsa praesentium
            quidem sint sit.
          </aeg-accordion-item>

          <aeg-accordion-item name="workspaces" title="Espaces">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque eveniet excepturi exercitationem facere
            id inventore, laborum minus nobis officiis pariatur recusandae reiciendis sed sit. Alias blanditiis
            consequuntur natus perferendis quod.
          </aeg-accordion-item>

          <aeg-accordion-item name="dashboards" title="Tableaux de bord">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Atque error facilis ipsam, nam non quaerat
            reiciendis? Animi assumenda consequuntur doloribus enim fugiat fugit magnam, nam officia officiis
            perferendis veritatis vitae!
          </aeg-accordion-item>

          <aeg-accordion-item name="documents" title="Documents">
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus accusantium animi aspernatur atque autem
            delectus ducimus ea excepturi impedit labore nam, nulla possimus qui quod repellat saepe soluta tempora
            veritatis.
          </aeg-accordion-item>
        </aeg-accordion>
        -->
      </div>
    </aside>

    <div :class="{'-ml-72': isSidebarClosed && !isMobile}" class="flex flex-col bg-primary-800 text-white h-screen w-full">
      <!-- navbar -->
      <nav class="flex justify-between items-center px-4 h-12 py-4">
        <!-- navbar left -->
        <div>
          <div v-if="isMobile" class="cursor-pointer rounded-full hover:bg-gray-600 duration-150 p-1" @click="toggle()">
            <MenuIcon class="h-5 w-5"/>
          </div>
        </div>

        <!-- navbar right -->
        <div>
          <template v-if="authStore.isLoggedIn">
            <Dropdown>
              <template #button>
                <div class="flex items-center gap-1 cursor-pointer rounded-full hover:bg-gray-600 duration-150 p-1">
                  <DotsVerticalIcon class="h-4 w-4"/>
                </div>
              </template>

              <DropdownItem :to="{name: 'profile'}">
                <UserCircleIcon class="h-5 w-5"/>
                <span>Profil</span>
              </DropdownItem>
              <DropdownItem @click="logout">
                <LogoutIcon class="h-5 w-5"/>
                <span>Deconnexion</span>
              </DropdownItem>
            </Dropdown>
          </template>

          <template v-else>
            <RouterLink #default="{navigate, href, isExactActive}" custom to="/login">
              <a :class="{'bg-gray-500': isExactActive}" :href="href" class="flex items-center gap-1 cursor-pointer rounded-full hover:bg-gray-600 duration-150 p-1" @click="navigate">
                <UserIcon class="h-4 w-4"/>
                <div>Log in</div>
              </a>
            </RouterLink>
          </template>
        </div>
      </nav>

      <header class="flex py-6 px-4 text-3xl tracking-tight font-bold bg-white text-gray-900">
        <h1>{{ data.title }}</h1>
      </header>

      <main class="flex flex-col p-4 bg-gray-100 scrollbar scrollbar-thin scrollbar-thumb-gray-600 scrollbar-track-gray-300 h-full w-full text-gray-900">
        <RouterView/>
      </main>
    </div>
  </section>
</template>

<script lang="ts" setup>
import { computed, reactive, ref, watch } from "vue";
import { useAppStore } from "@/stores/app";
import { breakpointsTailwind, onClickOutside, useBreakpoints, useTitle } from "@vueuse/core";
import { useRoute, useRouter } from "vue-router";
import { DotsVerticalIcon, LogoutIcon, MenuIcon, UserCircleIcon, UserIcon, XIcon } from "@heroicons/vue/solid";
import { useAuthStore } from "@/stores/auth";
import useAppLocalStorage from "@/composables/useAppLocalStorage";
import Dropdown from "@/components/shared/overlay/Dropdown.vue";
import DropdownItem from "@/components/shared/overlay/DropdownItem.vue";

interface Data {
  menuItems: Array<any>;
  opened: boolean;
  title: string;
}

/* COMPOSABLES */
const appStore = useAppStore();
const authStore = useAuthStore();
const { storageToken } = useAppLocalStorage();
const route = useRoute();
const router = useRouter();
const breakpoints = useBreakpoints(breakpointsTailwind);

/* DATA */
const data = reactive<Data>({
  menuItems: null,
  opened: false,
  title: null,
});

const drawer_sidebar = ref(null);
const isMobile = breakpoints.smaller("lg");

/* INIT */
onClickOutside(drawer_sidebar, (_event) => data.opened = false);
await appStore.getInformations();

data.menuItems = [
  { type: "heading", label: "Menu" },
  { type: "item", label: "Home", route: { name: "home" } },
  { type: "item", label: "Tasks", route: { name: "tasks" } },
  { type: "heading", label: "Admin" },
  { type: "item", label: "Utilisateurs connectés", route: { name: "admin-connected-users" } },
  { type: "item", label: "API", route: { name: "admin-swagger" } },
  { type: "heading", label: "DEV" },
  { type: "item", label: "Datatable", route: { name: "dev-datatable" } },
  { type: "item", label: "Modal", route: { name: "dev-modal" } },
];

/* COMPUTED */
const isSidebarClosed = computed(() => {
  return isMobile.value && !data.opened;
});

/* HOOKS */
watch(route,
  (_route) => {
    // Modification du titre dans l'onglet et récupération de la partie du titre pour affichage en titre de page
    const title = useTitle(_route.meta.title, { titleTemplate: "%s | " + appStore.informations.app.title });
    data.title = title.value;
  },
  { immediate: true },
);

watch(
  storageToken,
  async (_token) => {
    if (_token != null) {
      await authStore.getUser();
    }
  },
  { immediate: true },
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
  await authStore.logout();
  await router.push({ name: "login" });
};
</script>

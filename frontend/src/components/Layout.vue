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
        <template v-for="(menuItem, menuItemIndex) in menu" :key="`menuitem-${menuItemIndex}`">

          <template v-if="menuItem.children != null">
            <div class="font-thin py-2 text-sm text-gray-300 px-4">{{ menuItem.label }}</div>
            <RouterLink v-for="(child, childIndex) in menuItem.children" :key="`childitem-${menuItemIndex}-${childIndex}`" #default="{navigate, href, isExactActive}" :to="child.route" custom>
              <a :class="{'bg-gray-500': isExactActive}" :href="href" class="block cursor-pointer my-1 py-1 px-6 hover:bg-gray-600 duration-150" @click.prevent="() => { navigate(); data.opened = false}">{{ child.label }}</a>
            </RouterLink>
          </template>

          <RouterLink v-else #default="{navigate, href, isExactActive}" :to="menuItem.route" custom>
            <a :class="{'bg-gray-500': isExactActive}" :href="href" class="block cursor-pointer my-1 py-1 px-6 hover:bg-gray-600 duration-150" @click.prevent="() => { navigate(); data.opened = false}">{{ menuItem.label }}</a>
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
        <div class="flex items-center gap-2">
          <div class="font-bold">{{ breakpoint }}</div>

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
import { DotsVerticalIcon, LogoutIcon, MenuIcon, UserCircleIcon, UserIcon, XIcon } from "@heroicons/vue/solid";
import { breakpointsTailwind, onClickOutside, useBreakpoints, useTitle } from "@vueuse/core";

interface Data {
  opened: boolean;
  title: string;
}

/* COMPOSABLES */
const appStore = useAppStore();
const authStore = useAuthStore();
const { storageToken } = useAppLocalStorage();
const { dialog } = useAegir();
const route = useRoute();
const router = useRouter();
const breakpoints = useBreakpoints(breakpointsTailwind);
const { menu, refreshMenu } = useMenu();

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
onClickOutside(drawer_sidebar, (_event) => data.opened = false);
await appStore.getInformations();

/* COMPUTED */
const isSidebarClosed = computed(() => {
  return isMobile.value && !data.opened;
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
    },
  });
};
</script>

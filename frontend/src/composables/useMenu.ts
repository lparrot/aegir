import { ref } from "vue";
import { MenuItem } from "src/models/ui.model";
import { checkAccess } from "src/router";
import { Router, useRouter } from "vue-router";
import cloneDeep from "lodash/cloneDeep";

const defaultMenu = ref([]);
const isMenuLoaded = ref<boolean>(false);
const menu = ref<MenuItem[]>();
let routerInstance = null;

export const useMenu = (router?: Router) => {
  routerInstance = router ?? useRouter();

  const checkMenuItem = (menuItems: MenuItem[]) => {
    const indexesToRemove = [];

    // Parcours de tous les items de menu
    for (let menuItemIndex = 0; menuItemIndex < menuItems.length; menuItemIndex++) {
      const menuItem = menuItems[menuItemIndex];

      let isAccessOk = true;

      if (menuItem.showIf != null) {
        isAccessOk = typeof menuItem.showIf === "function" ? menuItem.showIf() : menuItem.showIf;
      } else if (menuItem.children != null) {
        // S'il y a des enfants, on appelle la fonction en récursif
        menuItem.children = checkMenuItem(menuItem.children);

        // S'il n'y a plus d'enfant, on flag l'item a supprimer
        if (menuItem.children.length < 1) {
          isAccessOk = false;
        }
      } else {
        if (menuItem.to != null) {
          // S'il y a un lien dans l'item, on appelle la fonction de vérification des droits
          isAccessOk = checkAccess(routerInstance.resolve(menuItem.to));
        } else {
          // S'il n'y a pas de lien dans l'item, on l'affiche
          isAccessOk = true;
        }
      }

      if (!isAccessOk) {
        // Si l'utilisateur n'a pas le droit
        indexesToRemove.push(menuItemIndex);
      }
    }

    // Suppression des items par rapport aux index
    return menuItems.filter((menuItem, menuItemIndex) => indexesToRemove.indexOf(menuItemIndex) < 0);
  };

  const refreshMenu = () => {
    menu.value = checkMenuItem(cloneDeep(defaultMenu.value));
  };

  const setMenuDefault = (menuItems: MenuItem[]) => {
    defaultMenu.value = menuItems;
    refreshMenu();
    isMenuLoaded.value = true;
  };

  return { isMenuLoaded, menu, refreshMenu, setMenuDefault };
};

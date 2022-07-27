import { ref } from "vue";
import { MenuItem } from "src/models/ui.model";
import { checkAccess } from "src/router";
import { useRouter } from "vue-router";
import cloneDeep from "lodash.clonedeep";

export const useMenu = (menuItems: MenuItem[] = []) => {
  const defaultMenu = menuItems;
  const router = useRouter();
  const menu = ref();

  const refreshMenu = async () => {
    menu.value = await checkMenuItem(cloneDeep(defaultMenu));
  };

  const checkMenuItem = async (menuItems: MenuItem[]) => {
    const indexesToRemove = [];

    // Parcours de tous les items de menu
    for (let menuItemIndex = 0; menuItemIndex < menuItems.length; menuItemIndex++) {
      const menuItem = menuItems[menuItemIndex];

      let isAccessOk = true;

      if (menuItem.showIf != null) {
        isAccessOk = typeof menuItem.showIf === "function" ? await menuItem.showIf() : menuItem.showIf;
      } else if (menuItem.children != null) {
        // S'il y a des enfants, on appelle la fonction en récursif
        menuItem.children = await checkMenuItem(menuItem.children);

        // S'il n'y a plus d'enfant, on flag l'item a supprimer
        if (menuItem.children.length < 1) {
          isAccessOk = false;
        }
      } else {
        if (menuItem.to != null) {
          // S'il y a un lien dans l'item, on appelle la fonction de vérification des droits
          isAccessOk = checkAccess(router.resolve(menuItem.to));
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

  return { menu, refreshMenu };
};

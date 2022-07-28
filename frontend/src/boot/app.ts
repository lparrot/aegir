import { useAppStore } from "stores/app";
import { boot } from "quasar/wrappers";
import { configure, defineRule, ErrorMessage, Field, FieldArray, Form } from "vee-validate";
import { localize, setLocale } from "@vee-validate/i18n";
import AllRules from "@vee-validate/rules";
import fr from "@vee-validate/i18n/dist/locale/fr.json";
import { useMenu } from "src/composables/useMenu";
import { useProjectStore } from "stores/project";
import useAppEventBus from "src/composables/useAppEventBus";

export default boot(async ({ app, router }) => {
  try {
    ////////////////
    // Composables
    ////////////////
    const appStore = useAppStore();
    const projectStore = useProjectStore();
    const bus = useAppEventBus();
    const { refreshMenu, setMenuDefault } = useMenu(router);

    app.component("Form", Form);
    app.component("Field", Field);
    app.component("FieldArray", FieldArray);
    app.component("ErrorMessage", ErrorMessage);

    Object.keys(AllRules).forEach(rule => {
      defineRule(rule, AllRules[rule]);
    });

    configure({
      generateMessage: localize({
        fr,
      }),
    });

    setLocale("fr");

    await appStore.fetchInformations();

    setMenuDefault([
      { type: "header", label: "Menu" },
      { icon: "home", label: "Accueil", to: { name: "index" } },
      { icon: "view_module", label: "Dashboard", to: { name: "dashboard" } },
    ]);

    bus.$on("connected", async () => {
      await projectStore.fetchItems();
      await projectStore.fetchSelectedItem();
      refreshMenu();
    });
  } catch (error) {
    console.error(error);
  }
});

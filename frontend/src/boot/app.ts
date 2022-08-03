import { boot } from "quasar/wrappers";
import { configure, defineRule, ErrorMessage, Field, FieldArray, Form } from "vee-validate";
import { localize, setLocale } from "@vee-validate/i18n";
import AllRules from "@vee-validate/rules";
import fr from "@vee-validate/i18n/dist/locale/fr.json";
import useMenu from "src/composables/useMenu";
import { useAppStore } from "stores/app";
import useWebsocket from "src/composables/useWebsocket";

const socket = useWebsocket();
const appStore = useAppStore();
const { setMenuDefault, setRouter } = useMenu();

export default boot(async ({ app, router }) => {
  ////////////////
  // Composables
  ////////////////

  setRouter(router);
  socket.initialize();

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
    { icon: "task_alt", label: "Tasks", to: { name: "tasks" } },
  ]);
});

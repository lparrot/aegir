import { useAppStore } from "stores/app";
import { boot } from "quasar/wrappers";
import { configure, defineRule, ErrorMessage, Field, FieldArray, Form } from "vee-validate";
import { localize, setLocale } from "@vee-validate/i18n";
import AllRules from "@vee-validate/rules";
import fr from "@vee-validate/i18n/dist/locale/fr.json";

export default boot(async ({ app }) => {
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

  const appStore = useAppStore();
  await appStore.fetchInformations();
});

<template>
  <div class="home">
    <div class="w-full max-w-md rounded-2xl bg-white p-2">
      <Disclosure #default="{ open }">
        <DisclosureButton class="flex w-full justify-between rounded-lg bg-purple-100 px-4 py-2 text-left text-sm font-medium text-purple-900 hover:bg-purple-200 focus:outline-none focus-visible:ring focus-visible:ring-purple-500 focus-visible:ring-opacity-75">
          <span>Variables d'environnement</span>
          <ChevronUpIcon :class="open ? 'rotate-180 transform' : ''" class="h-5 w-5 text-purple-500"/>
        </DisclosureButton>
        <DisclosurePanel class="px-4 pt-4 pb-2 text-sm text-gray-500">
          <table class="w-full">
            <caption class="hidden">Variables d'environnement</caption>
            <tr v-for="key in Object.keys(env)" :key="key" class="odd:bg-white even:bg-primary-100">
              <td>{{ key }}</td>
              <td class="pl-4">{{ env[key] }}</td>
            </tr>
          </table>
        </DisclosurePanel>
      </Disclosure>
      <Disclosure #default="{ open }" as="div" class="mt-2">
        <DisclosureButton class="flex w-full justify-between rounded-lg bg-purple-100 px-4 py-2 text-left text-sm font-medium text-purple-900 hover:bg-purple-200 focus:outline-none focus-visible:ring focus-visible:ring-purple-500 focus-visible:ring-opacity-75">
          <span>Do you offer technical support?</span>
          <ChevronUpIcon :class="open ? 'rotate-180 transform' : ''" class="h-5 w-5 text-purple-500"/>
        </DisclosureButton>
        <DisclosurePanel class="px-4 pt-4 pb-2 text-sm text-gray-500">
          No.
        </DisclosurePanel>
      </Disclosure>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { Disclosure, DisclosureButton, DisclosurePanel } from "@headlessui/vue";
import { ChevronUpIcon } from "@heroicons/vue/solid";

const env = import.meta.env;
</script>


<template>
  <div class="lg:flex lg:items-center lg:justify-between">
    <div class="flex-1 min-w-0">
      <h2 class="text-2xl font-bold leading-7 text-gray-900 sm:text-3xl sm:tracking-tight sm:truncate">{{ user.userData?.fullname }}</h2>
      <div class="mt-1 flex flex-col sm:flex-row sm:flex-wrap sm:mt-0 sm:space-x-6">
        <div class="mt-2 flex items-center text-sm text-gray-500">
          <BriefcaseIcon class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400"/>
          Full-time
        </div>
        <div class="mt-2 flex items-center text-sm text-gray-500">
          <MapPinIcon class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400"/>
          Remote
        </div>
        <div class="mt-2 flex items-center text-sm text-gray-500">
          <CurrencyDollarIcon class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400"/>
          $120k &ndash; $140k
        </div>
        <div class="mt-2 flex items-center text-sm text-gray-500">
          <CalendarIcon class="flex-shrink-0 mr-1.5 h-5 w-5 text-gray-400"/>
          Closing on January 9, 2020
        </div>
      </div>
    </div>

    <div class="mt-5 flex lg:mt-0 lg:ml-4">
      <div>
        <BaseButton class="flex" outlined type="button">
          <PencilIcon class="-ml-1 mr-2 h-5 w-5"/>
          Edit
        </BaseButton>
      </div>

      <div class="ml-3">
        <BaseButton class="flex" outlined type="button">
          <LinkIcon class="-ml-1 mr-2 h-5 w-5"/>
          View
        </BaseButton>
      </div>

      <div class="ml-3">
        <BaseButton class="flex" color="secondary" type="button">
          <CheckIcon class="-ml-1 mr-2 h-5 w-5"/>
          Publish
        </BaseButton>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { api } from "@/api";
import { BriefcaseIcon, CalendarIcon, CheckIcon, CurrencyDollarIcon, LinkIcon, MapPinIcon, PencilIcon } from "@heroicons/vue/24/solid";
import { UserInfo_Security } from "back_types";
import { ref } from "vue";

const user = ref<UserInfo_Security>(null);

const fetchUser = async () => {
  const { success, result } = await api.getAuthUserData();

  if (success) {
    user.value = result;
  }
};

await fetchUser();
</script>

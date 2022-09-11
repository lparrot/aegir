<template>
  <div class="grid grid-cols-1 gap-28">
    <!-- Hero section -->
    <div class="flex flex-col w-full items-center">
      <div class="text-6xl font-bold">
        Aegir
      </div>
      <div class="text-xl text-primary-400">
        Application de management d'équipe et d'application dans le domaine informatique.
      </div>
      <div class="flex gap-2 mt-6">
        <BaseButton class="text-xl !font-light" color="secondary" @click="router.push({name: 'tasks'})">
          Démarrer
        </BaseButton>
        <BaseButton class="text-xl !font-light">
          Démonstration
        </BaseButton>
      </div>
    </div>

    <hr/>

    <!-- Features -->
    <div class="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
      <div class="lg:text-center">
        <h2 class="text-lg font-semibold text-indigo-600">Transactions</h2>
        <p class="mt-2 text-3xl font-bold leading-8 tracking-tight text-gray-900 sm:text-4xl">A better way to send money</p>
        <p class="mt-4 max-w-2xl text-xl text-gray-500 lg:mx-auto">Lorem ipsum dolor sit amet consect adipisicing elit. Possimus magnam voluptatum cupiditate veritatis in accusamus quisquam.</p>
      </div>

      <div class="mt-10">
        <dl class="space-y-10 md:grid md:grid-cols-2 md:gap-x-8 md:gap-y-10 md:space-y-0">
          <div class="relative">
            <dt>
              <div class="absolute flex h-12 w-12 items-center justify-center rounded-md bg-indigo-500 text-white">
                <!-- Heroicon name: outline/globe-alt -->
                <svg aria-hidden="true" class="h-6 w-6" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path d="M12 21a9.004 9.004 0 008.716-6.747M12 21a9.004 9.004 0 01-8.716-6.747M12 21c2.485 0 4.5-4.03 4.5-9S14.485 3 12 3m0 18c-2.485 0-4.5-4.03-4.5-9S9.515 3 12 3m0 0a8.997 8.997 0 017.843 4.582M12 3a8.997 8.997 0 00-7.843 4.582m15.686 0A11.953 11.953 0 0112 10.5c-2.998 0-5.74-1.1-7.843-2.918m15.686 0A8.959 8.959 0 0121 12c0 .778-.099 1.533-.284 2.253m0 0A17.919 17.919 0 0112 16.5c-3.162 0-6.133-.815-8.716-2.247m0 0A9.015 9.015 0 013 12c0-1.605.42-3.113 1.157-4.418"
                        stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <p class="ml-16 text-lg font-medium leading-6 text-gray-900">Competitive exchange rates</p>
            </dt>
            <dd class="mt-2 ml-16 text-base text-gray-500">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Maiores impedit perferendis suscipit eaque, iste dolor cupiditate blanditiis ratione.</dd>
          </div>

          <div class="relative">
            <dt>
              <div class="absolute flex h-12 w-12 items-center justify-center rounded-md bg-indigo-500 text-white">
                <!-- Heroicon name: outline/scale -->
                <svg aria-hidden="true" class="h-6 w-6" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path
                    d="M12 3v17.25m0 0c-1.472 0-2.882.265-4.185.75M12 20.25c1.472 0 2.882.265 4.185.75M18.75 4.97A48.416 48.416 0 0012 4.5c-2.291 0-4.545.16-6.75.47m13.5 0c1.01.143 2.01.317 3 .52m-3-.52l2.62 10.726c.122.499-.106 1.028-.589 1.202a5.988 5.988 0 01-2.031.352 5.988 5.988 0 01-2.031-.352c-.483-.174-.711-.703-.59-1.202L18.75 4.971zm-16.5.52c.99-.203 1.99-.377 3-.52m0 0l2.62 10.726c.122.499-.106 1.028-.589 1.202a5.989 5.989 0 01-2.031.352 5.989 5.989 0 01-2.031-.352c-.483-.174-.711-.703-.59-1.202L5.25 4.971z"
                    stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <p class="ml-16 text-lg font-medium leading-6 text-gray-900">No hidden fees</p>
            </dt>
            <dd class="mt-2 ml-16 text-base text-gray-500">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Maiores impedit perferendis suscipit eaque, iste dolor cupiditate blanditiis ratione.</dd>
          </div>

          <div class="relative">
            <dt>
              <div class="absolute flex h-12 w-12 items-center justify-center rounded-md bg-indigo-500 text-white">
                <!-- Heroicon name: outline/bolt -->
                <svg aria-hidden="true" class="h-6 w-6" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path d="M3.75 13.5l10.5-11.25L12 10.5h8.25L9.75 21.75 12 13.5H3.75z" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <p class="ml-16 text-lg font-medium leading-6 text-gray-900">Transfers are instant</p>
            </dt>
            <dd class="mt-2 ml-16 text-base text-gray-500">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Maiores impedit perferendis suscipit eaque, iste dolor cupiditate blanditiis ratione.</dd>
          </div>

          <div class="relative">
            <dt>
              <div class="absolute flex h-12 w-12 items-center justify-center rounded-md bg-indigo-500 text-white">
                <svg aria-hidden="true" class="h-6 w-6" fill="none" stroke="currentColor" stroke-width="1.5" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <path d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 01.865-.501 48.172 48.172 0 003.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <p class="ml-16 text-lg font-medium leading-6 text-gray-900">Mobile notifications</p>
            </dt>
            <dd class="mt-2 ml-16 text-base text-gray-500">Lorem ipsum, dolor sit amet consectetur adipisicing elit. Maiores impedit perferendis suscipit eaque, iste dolor cupiditate blanditiis ratione.</dd>
          </div>
        </dl>
      </div>
    </div>

    <hr/>

    <!-- Testimonials -->
    <div class="container px-6 py-10 mx-auto">
      <div class="mt-6 md:flex md:items-center md:justify-between">
        <div>
          <h1 class="text-3xl font-semibold text-gray-800 capitalize xl:text-5xl lg:text-4xl dark:text-white">
            What our clients are saying </h1>

          <div class="flex mx-auto mt-6">
            <span class="inline-block w-40 h-1 bg-blue-500 rounded-full"></span>
            <span class="inline-block w-3 h-1 mx-1 bg-blue-500 rounded-full"></span>
            <span class="inline-block w-1 h-1 bg-blue-500 rounded-full"></span>
          </div>
        </div>

        <div class="flex justify-between mt-8 md:mt-0">
          <button class="p-2 mx-3 text-gray-800 transition-colors duration-300 border rounded-full rtl:-scale-x-100 dark:text-gray-200 dark:hover:bg-gray-800 dark:border-gray-700 hover:bg-gray-100">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path d="M15 19l-7-7 7-7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>

          <button class="p-2 text-gray-800 transition-colors duration-300 border rounded-full rtl:-scale-x-100 dark:text-gray-200 dark:hover:bg-gray-800 dark:border-gray-700 hover:bg-gray-100">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path d="M9 5l7 7-7 7" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
      </div>

      <section class="grid grid-cols-1 gap-8 mt-8 xl:mt-12 lg:!grid-cols-2 xl:!grid-cols-3">
        <div class="p-8 border rounded-lg dark:border-gray-700">
          <p class="leading-loose text-gray-500 dark:text-gray-400">
            “Lorem ipsum dolor sit amet, consectetur adipisicing elit. Tempore quibusdam ducimus libero ad
            tempora doloribus expedita laborum saepe voluptas perferendis delectus assumenda rerum, culpa
            aperiam dolorum, obcaecati corrupti aspernatur a.”. </p>

          <div class="flex items-center mt-8 -mx-2">
            <img alt="" class="object-cover mx-2 rounded-full w-14 shrink-0 h-14 ring-4 ring-gray-300 dark:ring-gray-700" src="https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=880&q=80">

            <div class="mx-2">
              <h1 class="font-semibold text-gray-800 dark:text-white">Robert</h1>
              <span class="text-sm text-gray-500 dark:text-gray-400">CTO, Robert Consultency</span>
            </div>
          </div>
        </div>

        <div class="p-8 bg-blue-500 border border-transparent rounded-lg dark:bg-blue-600">
          <p class="leading-loose text-white">
            “Lorem ipsum dolor sit amet, consectetur adipisicing elit. Tempore quibusdam ducimus libero ad
            tempora doloribus expedita laborum saepe voluptas perferendis delectus assumenda rerum, culpa
            aperiam dolorum, obcaecati corrupti aspernatur a.”. </p>

          <div class="flex items-center mt-8 -mx-2">
            <img alt="" class="object-cover mx-2 rounded-full w-14 shrink-0 h-14 ring-4 ring-blue-200" src="https://images.unsplash.com/photo-1531590878845-12627191e687?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80">

            <div class="mx-2">
              <h1 class="font-semibold text-white">Jeny Doe</h1>
              <span class="text-sm text-blue-200">CEO, Jeny Consultency</span>
            </div>
          </div>
        </div>

        <div class="p-8 border rounded-lg dark:border-gray-700">
          <p class="leading-loose text-gray-500 dark:text-gray-400">
            “Lorem ipsum dolor sit amet, consectetur adipisicing elit. Tempore quibusdam ducimus libero ad
            tempora doloribus expedita laborum saepe voluptas perferendis delectus assumenda rerum, culpa
            aperiam dolorum, obcaecati corrupti aspernatur a.”. </p>

          <div class="flex items-center mt-8 -mx-2">
            <img alt="" class="object-cover mx-2 rounded-full w-14 shrink-0 h-14 ring-4 ring-gray-300 dark:ring-gray-700" src="https://images.unsplash.com/photo-1488508872907-592763824245?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80">

            <div class="mx-2">
              <h1 class="font-semibold text-gray-800 dark:text-white">Ema Watson </h1>
              <span class="text-sm text-gray-500 dark:text-gray-400">Marketing Manager at Stech</span>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script lang="ts" setup>

</script>

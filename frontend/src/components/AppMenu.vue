<template>
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
</template>

<script lang="ts" setup>
const { menu } = useMenu();
</script>

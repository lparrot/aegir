<template>
  <q-list v-if="isMenuLoaded">
    <template v-for="(menuItem, menuItemIndex) in menu" :key="'item' + menuItemIndex">
      <q-separator v-if="menuItem.type === 'separator'" class="q-my-sm" inset/>

      <q-item-label v-else-if="menuItem.type === 'header'" header>{{ menuItem.label }}</q-item-label>

      <template v-else-if="menuItem.children != null">
        <q-expansion-item :default-opened="menuItem.defaultOpened ?? true" :icon="menuItem.icon" :label="menuItem.label" dense>
          <q-item v-for="(subMenuItem, subMenuItemIndex) in menuItem.children" :key="`subitem${menuItemIndex}-${subMenuItemIndex}`" v-ripple :to="subMenuItem.to ?? null" clickable dense inset-level="0.5" @click="subMenuItem.action ? subMenuItem.action() : null">
            <q-item-section avatar>
              <q-icon :name="subMenuItem.icon"/>
            </q-item-section>
            <q-item-section>
              {{ subMenuItem.label }}
            </q-item-section>
          </q-item>
        </q-expansion-item>
      </template>

      <template v-else>
        <q-item v-ripple :to="menuItem.to ?? null" clickable dense @click="menuItem.action ? menuItem.action() : null">
          <q-item-section avatar>
            <q-icon :name="menuItem.icon"/>
          </q-item-section>
          <q-item-section>
            {{ menuItem.label }}
          </q-item-section>
        </q-item>
      </template>
    </template>
  </q-list>
</template>

<script lang="ts" setup>
import { useMenu } from "src/composables/useMenu";

const { menu, isMenuLoaded } = useMenu();
</script>

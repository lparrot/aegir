<template>
  <Teleport to="body">
    <template v-if="show">
      <div class="absolute top-0 left-0 h-screen w-screen bg-black/50 backdrop-blur-sm"></div>
      <div class="absolute top-0 left-0 h-screen w-screen flex flex-col items-center justify-center font-bold">
        <component :is="options.icon" :class="options.iconClass"></component>
        <div :class="options.messageClass">{{ options.message }}</div>
      </div>
    </template>
  </Teleport>
</template>

<script lang="ts" setup>
const { loader } = useAegir();
const { show, options } = loader;
</script>

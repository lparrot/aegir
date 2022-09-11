<template>
  <section class="flex items-center justify-between p-3">
    <RouterLink :to="{name:'home'}" class="flex items-center gap-2">
      <img alt="logo" class="h-12 w-12" src="/logo.png">
    </RouterLink>

    <div class="flex items-center gap-2">
      <RouterLink :to="{name: 'login'}">
        <BaseButton color="secondary" outlined>
          <span>Connexion</span>
        </BaseButton>
      </RouterLink>
      <RouterLink :to="{name: ''}">
        <BaseButton color="secondary">
          <span>Enregistrement</span>
        </BaseButton>
      </RouterLink>
    </div>
  </section>

  <div class="p-8">
    <router-view/>
  </div>
</template>

<script lang="ts" setup>

</script>

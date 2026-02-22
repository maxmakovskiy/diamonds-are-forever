<script setup>
import { useAuthStore } from '@/stores'
import { router } from '@/router'

const authStore = useAuthStore();
if (authStore.user) {
  router.push('/items');
}

</script>


<template>
<main id="app-container">
    <!-- TOOD: tmp comment -->
    <!-- <aside v-show="authStore.user" id="header"> -->
    <aside id="header">
      <router-link to="/items">
        Items
      </router-link>
      <router-link to="/actions">
        Actions
      </router-link>
      <router-link to="/profile">
        Profile
      </router-link>
    </aside>
    
    <router-view />

</main>
</template>

<style scoped>
#app-container {
  height: 100%;
  width: 100%;
}

#header {
  height: 10%;
  border-bottom: 2px solid black;
  box-shadow: 0 2px 0 #778da9;

  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  column-gap: 2em;
}

</style>

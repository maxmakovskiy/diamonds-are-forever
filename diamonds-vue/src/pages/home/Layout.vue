<script setup>
import { useAuthStore } from '@/stores'
import { router } from '@/router'

const authStore = useAuthStore();
if (authStore.user) {
    router.push('/items');
}

</script>

<template>
<div class="page-container">
    <div class="nav-header">
      <router-link to="/items">
        Items
      </router-link>
      <!-- <router-link :to="{ name: 'actions' }">
        Actions
      </router-link>
      <router-link :to="{ name: 'account' }">
        User profile
      </router-link> -->
    </div>
  
    <div class="page-data">
      <router-view />
    </div>

</div>
</template>

<style scoped>
.nav-header {
  height: 10vh;
  padding: 2em;
}

.page-data {
  margin: 1rem 2rem;
}

</style>
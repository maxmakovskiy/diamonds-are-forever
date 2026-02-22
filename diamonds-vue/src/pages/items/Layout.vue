<script setup>
import { useAuthStore } from '@/stores'
import { router } from '@/router'

const authStore = useAuthStore();
if (authStore.user) {
  router.push('/items');
}

</script>

<template>
<div id="page-container">
  <aside id="page">
      <router-view />
  </aside>

  <button id="new-item">New</button>
</div>
</template>

<style scoped>

#page-container {
  display: flex;
  flex-direction: row;

  height: 90%;
}

#page {
  padding: 2em;
  overflow: scroll;
}

#new-item {
  position: absolute;
  right: 2rem;
  bottom: 2rem;

  background-color: white;
  border: 3px solid #363537;
  border-radius: 5px;

  font-size: 1.5rem;
  padding: 0.5rem;

  box-shadow: 2px 3px 3px #2E3532;
}

#new-item:hover {
  cursor: pointer;
  background-color: #D8DDEF;
}

</style>
<script setup>
import { storeToRefs } from 'pinia';

import { useAlertStore } from '@/stores';

const alertStore = useAlertStore();
const { alert } = storeToRefs(alertStore);
</script>

<template>
    <div v-if="alert" id="alert-container">
      <button @click="alertStore.clear()">&times;</button>
      <p><strong>{{alert.message}}</strong></p>
    </div>
</template>

<style scoped>

#alert-container {
  font-size: 0.8em;

  background-color: #ffad99;
  color: #8d0801;
  border: 3px solid #8d0801;
  border-radius: 5px;
  padding: 0.3em 0;

  display: flex;
  flex-direction: row;
  align-items: baseline;
  justify-content: space-evenly;
}

#alert-container > button {
  background: transparent;
  color: #8d0801;
  border: 3px solid #8d0801;
  border-radius: 5px;
  font-weight: bold;
}


#alert-container > button:hover {
  background: #8d0801;
  color: #ffad99;
  border: 3px solid #8d0801;
  cursor: pointer;
}

</style>
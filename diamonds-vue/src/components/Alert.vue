<script setup>
import { storeToRefs } from 'pinia';

import { useAlertStore } from '@/stores';

const alertStore = useAlertStore();
const { alert } = storeToRefs(alertStore);
</script>

<template>
    <div v-if="alert" id="alert-container" :class="alert.type">
      <button @click="alertStore.clear()">&times;</button>
      <p><strong>{{alert.message}}</strong></p>
    </div>
</template>

<style scoped>

#alert-container {
  font-size: 0.8em;

  display: flex;
  flex-direction: row;
  align-items: baseline;
  justify-content: space-evenly;
}

#alert-container > button {
  background: transparent;
  border-radius: 5px;
  font-weight: bold;
  margin-right: 0.3em;
}

#alert-container > button:hover {
  cursor: pointer;
}

.alert-danger {
  background-color: #ffad99;
  color: #8d0801;
  border: 3px solid #8d0801;
  border-radius: 5px;
  padding: 0.3em 0;
}

.alert-danger > button {
  color: #8d0801;
  border: 3px solid #8d0801;
}

.alert-danger > button:hover {
  background: #8d0801;
  color: black;
  border: 3px solid black;
}

.alert-success {
  background-color: #b3f2b5;
  color: #209623;
  border: 3px solid #209623;
  border-radius: 5px;
  padding: 0.3em 0.2em;
}

.alert-success > button {
  color: #209623;
  border: 3px solid #209623;
}

.alert-success > button:hover {
  background: #209623;
  color: black;
  border: 3px solid black;
}

</style>
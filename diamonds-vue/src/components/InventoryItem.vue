<script setup>
import { defineProps, computed } from 'vue';
import { router } from '@/router'

const props = defineProps(['item'])


function capitalize(val) {
  return String(val).charAt(0).toUpperCase() + String(val).slice(1);
}

const title = computed(() => capitalize(props.item.type))

function details() {
  router.push({ name: 'item_details', params: { id: props.item.lotId } })
}

</script>

<template>
  <div @click="details()" class="item-container">
    <h3>{{ title }}</h3>
    <h4>{{ `Stock name: ${ item.stockName }` }}</h4>

    <div>Origin: {{ item.origin }}</div>
    <div>Purchase date: {{ item.purchaseDate }}</div>

  </div>
</template>

<style scoped>
.item-container {
  border: 2px solid black;
  border-radius: 5px;
  padding: 1em;

  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.item-container:hover {
  box-shadow: 5px 3px 3px gray;
  cursor: pointer;
}

</style>